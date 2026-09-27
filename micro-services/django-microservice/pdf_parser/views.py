from django.http.request import HttpRequest
from django.http.response import JsonResponse
from django.views.decorators.csrf import csrf_exempt
import json
from create_pdf.create_pdf import create_incident_report_pdf

# dummy_data.py

dummy_report_data = {
    "report": {
        "report_id": "IR-2023-10-045",
        "date_filed": "October 24, 2023",
        "incident_date": "October 24, 2023",
        "incident_time": "10:30 AM",
        "location": "Main Campus - 3rd Floor, CCS Laboratory Room 302",
        "severity": "Medium"  # Options: High, Medium, Low 
    },
    "involved_parties": [
        {
            "id_number": "18304921",
            "name": "Juan Dela Cruz",
            "role": "Student",
            "department": "BS Information Technology",
            "contact": "0917-123-4567"
        },
        {
            "id_number": "EMP-9281",
            "name": "Maria Santos",
            "role": "Faculty",
            "department": "College of Computer Studies",
            "contact": "0918-987-6543"
        }
    ],
    "details": {
        "description": "During the laboratory session for IT 312, student Juan Dela Cruz accidentally tripped over a loose power extension cord. This caused computer unit PC-14 to be pulled from the desk and fall to the floor. The monitor screen cracked, and the system unit casing was dented. No physical injuries were sustained by the student.",
        "action_taken": "1. Faculty immediately powered off the main breaker for the row to prevent electrical hazards.\n2. Campus security was notified, and the area was cordoned off.\n3. The damaged unit was endorsed to the MIS department for hardware assessment.\n4. Work order submitted for the loose extension cord."
    },
    "preparer": {
        "name": "Pedro Penduko",
        "title": "Laboratory Custodian",
        "department": "MIS Department"
    }
}


def pdf_parser_helloworld(request: HttpRequest):
    return JsonResponse({ "message": "Hello world ;D" })

def pdf_parser_health(request: HttpRequest):
    return JsonResponse({ "health_status": "Healthy" })

@csrf_exempt #FOR DEVELOPMENT PHASE ONLY;
def create_pdf_report(request: HttpRequest):
    if request.method == 'POST':
        payload = dummy_report_data
        return create_incident_report_pdf(payload)
    else:  
        return JsonResponse({ "GET": "USE POST METHOD" })
