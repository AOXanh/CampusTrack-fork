from django.http.request import HttpRequest
from django.http.response import JsonResponse


def health(request: HttpRequest):
  return JsonResponse({ "status": "Healthy" })