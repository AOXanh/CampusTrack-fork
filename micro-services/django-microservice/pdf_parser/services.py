from django.template.loader import render_to_string
from playwright.sync_api import sync_playwright
from dotenv import load_dotenv
from jinja2 import Template
import os, io, uuid, json

# Google Drive OAuth Imports
from google_auth_oauthlib.flow import InstalledAppFlow
from google.auth.transport.requests import Request
from googleapiclient.http import MediaIoBaseUpload
from google.oauth2.credentials import Credentials
from googleapiclient.discovery import build

import pdf_parser

load_dotenv()

_SCOPES: list[str | None] = [os.environ.get("SCOPES")]
_FOLDER_ID: str | None = os.environ.get("FOLDER_ID")
_TOKEN: str | None = os.environ.get("TOKEN")
_CLIENT_SECRET: str | None = os.environ.get("CLIENT_SECRET")

"""
This private function uploads the PDF file to Drive.

Parameters: pdf_bytes (bytes), filename (str)
Returns: str (the link of the uploaded PDF file link)
"""
def _upload_pdf_to_drive(pdf_bytes: bytes, filename: str) -> str:
    creds = None

    if _TOKEN:
        token_info = json.loads(_TOKEN)
        creds = Credentials.from_authorized_user_info(token_info, _SCOPES)

    # Check if credentials are not valid
    if not creds or not creds.valid:
        if creds and creds.expired and creds.refresh_token:
            creds.refresh(Request())
            print("==============================")
            print("TOKEN REFRESHED! Update your .env TOKEN with this:")
            print(creds.to_json())
        else:
            if _CLIENT_SECRET is None:
                raise ValueError("Missing CLIENT_SECRET in .env")

            client_config = json.loads(_CLIENT_SECRET)

            flow = InstalledAppFlow.from_client_config(client_config, _SCOPES)
            creds = flow.run_local_server(port=0)

            print("==============================")
            print("NEW TOKEN GENERATED! Add this to your .env TOKEN:")
            print(creds.to_json())

    service = build('drive', 'v3', credentials=creds)

    pdf_stream = io.BytesIO(pdf_bytes)
    file_metadata = {'name': filename, 'parents': [_FOLDER_ID]}
    media = MediaIoBaseUpload(pdf_stream, mimetype='application/pdf', resumable=True)
    file = service.files().create(body=file_metadata, media_body=media, fields='id, webViewLink').execute()

    file_id = file.get('id')
    permission = {'type': 'anyone', 'role': 'reader'}
    service.permissions().create(fileId=file_id,body=permission,fields='id').execute()

    return file.get('webViewLink')

"""
This function creates the incident report PDF

Dict required properties: { "report", "involved_parties", "details" and "preparer" }
Parameters: data (dict)
Returns: str (the google drive link)
"""
def create_incident_report_pdf(data: dict) -> tuple:
    template_string = render_to_string("pdf/incident_report_pdf.html")
    template = Template(template_string)

    rendered_html = template.render(
        report=data.get("report", {}),
        involved_parties=data.get("involved_parties", []),
        details=data.get("details", {}),
        preparer=data.get("preparer", {})
    )

    with sync_playwright() as p:
        browser = p.chromium.launch()
        page = browser.new_page()
        page.set_content(rendered_html)
        pdf_bytes = page.pdf(format="A4", print_background=True)
        browser.close()

    random_hex = uuid.uuid4().hex[:8]
    filename = f"UC_Incident_Report_{random_hex}.pdf"

    try:
        gdrive_url = _upload_pdf_to_drive(pdf_bytes, filename)
        return (filename, gdrive_url)
    except Exception as e:
        raise Exception(e)