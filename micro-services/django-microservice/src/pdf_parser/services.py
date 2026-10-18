import time, uuid
from django.template.loader import render_to_string
from playwright.sync_api import sync_playwright
from core.supabase_client import get_supabase_client


_SUPABASE = get_supabase_client()
_PDF_TEMPLATE_PATH = "pdf/incident_report_pdf.html"


def _upload_pdf_to_supabase(pdf_bytes: bytes, filename: str, bucket_name: str = "PDFs") -> str:
    destination_path = f"{int(time.time())}-{filename}"

    try:
      _SUPABASE.storage.from_(bucket_name).upload(path=destination_path, file=pdf_bytes)
      public_url = _SUPABASE.storage.from_(bucket_name).get_public_url(destination_path)
      return public_url
    except Exception as e:
        print(f"Upload failed: {e}")
        raise


def create_incident_report_pdf(data: dict) -> tuple[str, str]:
    rendered_html = render_to_string(_PDF_TEMPLATE_PATH, {
        "report": data.get("report", {}),
        "involved_parties": data.get("involved_parties", []),
        "details": data.get("details", {}),
        "preparer": data.get("preparer", {}),
    })

    with sync_playwright() as p, p.chromium.launch() as browser:
        page = browser.new_page()
        page.set_content(rendered_html, wait_until="networkidle")
        pdf_bytes = page.pdf(format="A4", print_background=True)

    filename = f"UC_Incident_Report_{uuid.uuid4().hex[:8]}.pdf"
    supabase_url = _upload_pdf_to_supabase(pdf_bytes, filename)

    return filename, supabase_url