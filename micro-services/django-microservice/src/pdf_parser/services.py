import time
import uuid
import os
from django.template.loader import render_to_string
from playwright.sync_api import sync_playwright
from dotenv import load_dotenv
from supabase import create_client, Client

load_dotenv()

SUPABASE_URL = os.getenv("SUPABASE_URL")
SUPABASE_KEY = os.getenv("SUPABASE_KEY")
supabase: Client = create_client(SUPABASE_URL, SUPABASE_KEY)


"""
This private function uploads the PDF bytes directly to Supabase Storage.

Parameters: pdf_bytes (bytes), filename (str), bucket_name (str)
Returns: str (the public URL of the uploaded PDF file)
"""
def _upload_pdf_to_supabase(pdf_bytes: bytes, filename: str, bucket_name: str = "pdfs") -> str:
    destination_path = f"public/{int(time.time())}-{filename}"
    print(f"Attempting to upload '{filename}' to Supabase bucket '{bucket_name}'...")

    try:
        # Upload the raw bytes directly to Supabase
        supabase.storage.from_(bucket_name).upload(
            path=destination_path,
            file=pdf_bytes,
            file_options={"content-type": "application/pdf"}
        )
        print(f"Successfully uploaded")
        
        # Get the public URL 
        public_url = supabase.storage.from_(bucket_name).get_public_url(destination_path)
        return public_url

    except Exception as e:
        print(f"Upload failed: {e}")
        raise


"""
This public function creates the incident report PDF and uploads it.

Dict required properties: { "report", "involved_parties", "details" and "preparer" }
Parameters: data (dict)
Returns: tuple[str, str] (The filename and the Supabase storage view link)
"""
def create_incident_report_pdf(data: dict) -> tuple[str, str]:
    # Render the Django template with the actual report data
    rendered_html = render_to_string("pdf/incident_report_pdf.html", {
        "report": data.get("report", {}),
        "involved_parties": data.get("involved_parties", []),
        "details": data.get("details", {}),
        "preparer": data.get("preparer", {}),
    })

    # Use context managers to automatically handle launching and closing the browser
    with sync_playwright() as p, p.chromium.launch() as browser:
        page = browser.new_page()
        page.set_content(rendered_html, wait_until="networkidle")

        # Debug: check whether the rendered page contains visible text
        print("Rendered page title:", page.title())
        print("Rendered page text:", page.locator("body").inner_text()[:1000])

        pdf_bytes = page.pdf(format="A4", print_background=True)

    filename = f"UC_Incident_Report_{uuid.uuid4().hex[:8]}.pdf"
    
    supabase_url = _upload_pdf_to_supabase(pdf_bytes, filename, bucket_name="incident")

    return filename, supabase_url