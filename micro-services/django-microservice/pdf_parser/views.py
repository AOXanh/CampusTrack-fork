from django.http.request import HttpRequest
from django.http.response import JsonResponse


def pdf_parser_helloworld(request: HttpRequest):
    return JsonResponse({ "message": "Hello world ;D" })

def pdf_parser_health(request: HttpRequest):
    return JsonResponse({ "health_status": "Healthy" })