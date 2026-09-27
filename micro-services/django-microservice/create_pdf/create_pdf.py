import os
import io
import uuid
from dotenv import load_dotenv
import json
from jinja2 import Template
from playwright.sync_api import sync_playwright
from django.http import JsonResponse 

# Google Drive OAuth Imports
from google.oauth2.credentials import Credentials
from google_auth_oauthlib.flow import InstalledAppFlow
from google.auth.transport.requests import Request
from googleapiclient.discovery import build
from googleapiclient.http import MediaIoBaseUpload
from create_pdf.get_template import get_template

# Load environment variables
load_dotenv() # returns: bool

# os.environ.get(key: str) -> str | None
SCOPES = [os.environ.get("SCOPES")] # SCOPES: list[str]
FOLDER_ID = os.environ.get("FOLDER_ID") # FOLDER_ID: str


def upload_pdf_to_drive(pdf_bytes: bytes, filename: str) -> str:
    creds = None
    
    # os.environ.get(key: str) -> str | None
    token_str = os.environ.get("TOKEN") # token_str: str | None
    
    if token_str:
        # json.loads(s: str) -> dict
        token_info = json.loads(token_str) # token_info: dict
        
        # Credentials.from_authorized_user_info(info: dict, scopes: list) -> Credentials
        creds = Credentials.from_authorized_user_info(token_info, SCOPES) # creds: Credentials
        
    # Check if credentials are not valid
    if not creds or not creds.valid:
        if creds and creds.expired and creds.refresh_token:
            # creds.refresh(request: Request) -> None
            creds.refresh(Request())
            
            print("TOKEN REFRESHED! Update your .env TOKEN with this:")
            # creds.to_json() -> str
            print(creds.to_json()) 
        else:
            # os.environ.get(key: str) -> str | None
            client_secret_str = os.environ.get("CLIENT_SECRTE") or os.environ.get("CLIENT_SECRET") # client_secret_str: str | None
            
            if not client_secret_str:
                raise ValueError("Missing CLIENT_SECRTE in .env")
                
            # json.loads(s: str) -> dict
            client_config = json.loads(client_secret_str) # client_config: dict
            
            # InstalledAppFlow.from_client_config(client_config: dict, scopes: list) -> InstalledAppFlow
            flow = InstalledAppFlow.from_client_config(client_config, SCOPES) # flow: InstalledAppFlow
            
            # flow.run_local_server(port: int) -> Credentials
            creds = flow.run_local_server(port=0) # creds: Credentials
            
            print("NEW TOKEN GENERATED! Add this to your .env TOKEN:")
            # creds.to_json() -> str
            print(creds.to_json()) 


    # build(serviceName: str, version: str, credentials: Credentials) -> Resource
    service = build('drive', 'v3', credentials=creds) # service: Resource

    file_metadata = {
        'name': filename,
        'parents': [FOLDER_ID]
    } # file_metadata: dict
    
    # io.BytesIO(initial_bytes: bytes) -> BytesIO
    pdf_stream = io.BytesIO(pdf_bytes) # pdf_stream: BytesIO
    
    # MediaIoBaseUpload(fd: BytesIO, mimetype: str, resumable: bool) -> MediaIoBaseUpload
    media = MediaIoBaseUpload(pdf_stream, mimetype='application/pdf', resumable=True) # media: MediaIoBaseUpload

    # service.files().create(body: dict, media_body: MediaIoBaseUpload, fields: str) -> HttpRequest
    # .execute() -> dict
    file = service.files().create(
        body=file_metadata,
        media_body=media,
        fields='id, webViewLink'
    ).execute() # file: dict
    
    # file.get(key: str) -> str
    file_id = file.get('id') # file_id: str

    permission = {'type': 'anyone', 'role': 'reader'} # permission: dict
    
    # service.permissions().create(fileId: str, body: dict, fields: str) -> HttpRequest
    # .execute() -> dict
    service.permissions().create(
        fileId=file_id, 
        body=permission, 
        fields='id'
    ).execute()

    # file.get(key: str) -> str
    return file.get('webViewLink')


# ==========================================
# 3. PDF GENERATOR FUNCTION
# ==========================================
def create_incident_report_pdf(data: dict) -> JsonResponse: 
    
    # get_template() -> str
    template_string = get_template() # template_string: str
    
    # Template(source: str) -> Template
    template = Template(template_string) # template: Template
    
    # template.render(kwargs: dict) -> str
    # data.get(key: str, default: any) -> dict | list
    rendered_html = template.render(
        report=data.get("report", {}),
        involved_parties=data.get("involved_parties", []),
        details=data.get("details", {}),
        preparer=data.get("preparer", {})
    ) # rendered_html: str
    
    # sync_playwright() -> PlaywrightContextManager
    with sync_playwright() as p:
        # p.chromium.launch() -> Browser
        browser = p.chromium.launch() # browser: Browser
        
        # browser.new_page() -> Page
        page = browser.new_page() # page: Page
        
        # page.set_content(html: str) -> None
        page.set_content(rendered_html)
        
        # page.pdf(format: str, print_background: bool) -> bytes
        pdf_bytes = page.pdf(format="A4", print_background=True) # pdf_bytes: bytes
        
        # browser.close() -> None
        browser.close()
        
    # uuid.uuid4() -> UUID
    # .hex -> str
    random_hex = uuid.uuid4().hex[:8] # random_hex: str
    
    filename = f"UC_Incident_Report_{random_hex}.pdf" # filename: str
    
    try:
        # upload_pdf_to_drive(pdf_bytes: bytes, filename: str) -> str
        gdrive_url = upload_pdf_to_drive(pdf_bytes, filename) # gdrive_url: str
        
        # JsonResponse(data: dict) -> JsonResponse
        return JsonResponse({
            "status": "success", 
            "filename": filename,
            "Link": gdrive_url
        })
    except Exception as e:
        # str(object: Exception) -> str
        error_msg = str(e) # error_msg: str
        
        # JsonResponse(data: dict, status: int) -> JsonResponse
        return JsonResponse({
            "status": "error",
            "message": f"Google Drive upload failed: {error_msg}"
        }, status=500)