from typing import Callable
from django.http.request import HttpRequest
from django.http.response import HttpResponse


"""
NOTE: DO NOT VIOLATE THE STRUCTURE LAYER

JL Tasks:

- Pag add ug user authentication ari bai gamit ang same JWT token sa backend
"""
class PdfParserMiddleware:
  def __init__(self, get_response: Callable[[HttpRequest], HttpResponse]):
    self.get_response = get_response

  def __call__(self, request: HttpRequest) -> HttpResponse:
    print("Hello world ana si middleware ;D")
    return self.get_response(request)