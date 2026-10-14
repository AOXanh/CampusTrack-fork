import json
from django.http.request import HttpRequest
from django.http.response import JsonResponse
from django.views.decorators.csrf import csrf_exempt
from django.views.decorators.http import require_POST
from pydantic import TypeAdapter, ValidationError
from .schemas import CreateIncidentReportRequest, CreateIncidentReportResponse
from pdf_parser import services


_incident_report_request_adapter = TypeAdapter(CreateIncidentReportRequest)


def pdf_parser_helloworld(request: HttpRequest):
    return JsonResponse({ "message": "Hello world ;D" })


@csrf_exempt # FOR DEVELOPMENT PHASE ONLY;
@require_POST
def create_pdf_report(request: HttpRequest):
  try:
    payload = json.loads(request.body)
  except json.JSONDecodeError:
    return JsonResponse({"message": "Request body must be valid JSON"}, status=400)

  try:
    report_data = _incident_report_request_adapter.validate_python(payload)
  except ValidationError as e:
    return JsonResponse({"message": "Invalid incident report data"}, status=400)

  filename, link = services.create_incident_report_pdf(report_data)
  response_body: CreateIncidentReportResponse = { "filename": filename, "link": link }
  return JsonResponse(response_body)