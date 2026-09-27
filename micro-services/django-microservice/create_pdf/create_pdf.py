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

load_dotenv()

# Google expects SCOPES to be a list, so we wrap the string from .env in brackets
SCOPES = [os.environ.get("SCOPES")]
FOLDER_ID = os.environ.get("FOLDER_ID")


def upload_pdf_to_drive(pdf_bytes, filename):
    creds = None
    
    # Load Token from .env
    token_str = os.environ.get("TOKEN")
    if token_str:
        token_info = json.loads(token_str)
        # Use from_authorized_user_info to load from the dictionary
        creds = Credentials.from_authorized_user_info(token_info, SCOPES)
        
    # If no valid credentials available, let the user log in or refresh.
    if not creds or not creds.valid:
        if creds and creds.expired and creds.refresh_token:
            creds.refresh(Request())
            # Print the new token so you can manually copy/paste it into your .env
            print("TOKEN REFRESHED! Update your .env TOKEN with this:")
            print(creds.to_json())
        else:
            # Look for CLIENT_SECRTE (matching your .env typo) or CLIENT_SECRET
            client_secret_str = os.environ.get("CLIENT_SECRTE") or os.environ.get("CLIENT_SECRET")
            if not client_secret_str:
                raise ValueError("Missing CLIENT_SECRTE in .env")
                
            client_config = json.loads(client_secret_str)
            
            # Use from_client_config to load from the dictionary
            flow = InstalledAppFlow.from_client_config(client_config, SCOPES)
            creds = flow.run_local_server(port=0)
            
            # Print the new token so you can manually copy/paste it into your .env
            print("NEW TOKEN GENERATED! Add this to your .env TOKEN:")
            print(creds.to_json())



    # Proceed with Google Drive API Upload
    service = build('drive', 'v3', credentials=creds)

    file_metadata = {
        'name': filename,
        'parents': [FOLDER_ID]
    }
    
    media = MediaIoBaseUpload(io.BytesIO(pdf_bytes), mimetype='application/pdf', resumable=True)

    file = service.files().create(
        body=file_metadata,
        media_body=media,
        fields='id, webViewLink'
    ).execute()
    
    file_id = file.get('id')

    # Make the file readable to anyone with the link
    permission = {'type': 'anyone', 'role': 'reader'}
    service.permissions().create(
        fileId=file_id, 
        body=permission, 
        fields='id'
    ).execute()

    return file.get('webViewLink')


# ==========================================
# 3. PDF GENERATOR FUNCTION
# ==========================================
def create_incident_report_pdf(data):
    # Render HTML with Jinja2
    template = Template(get_template())
    rendered_html = template.render(
        report=data.get("report", {}),
        involved_parties=data.get("involved_parties", []),
        details=data.get("details", {}),
        preparer=data.get("preparer", {})
    )
    
    # Generate PDF in memory using Playwright
    with sync_playwright() as p:
        browser = p.chromium.launch()
        page = browser.new_page()
        page.set_content(rendered_html)
        pdf_bytes = page.pdf(format="A4", print_background=True)
        browser.close()
        
    # Generate a unique filename
    filename = f"UC_Incident_Report_{uuid.uuid4().hex[:8]}.pdf"
    
    # Upload to Google Drive and get the link
    try:
        gdrive_url = upload_pdf_to_drive(pdf_bytes, filename)
        return JsonResponse({
            "status": "success", 
            "filename": filename,
            "Link": gdrive_url
        })
    except Exception as e:
        return JsonResponse({
            "status": "error",
            "message": f"Google Drive upload failed: {str(e)}"
        }, status=500)