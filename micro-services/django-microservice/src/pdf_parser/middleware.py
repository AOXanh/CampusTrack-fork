from typing import Callable
import jwt
from django.conf import settings
from django.core.exceptions import ImproperlyConfigured
from django.http.request import HttpRequest
from django.http.response import HttpResponse, JsonResponse


"""
NOTE: DO NOT VIOLATE THE STRUCTURE LAYER

JL Tasks:

- Pag add ug user authentication ari bai gamit ang same JWT token sa backend
"""
class PdfParserMiddleware:
  def __init__(self, get_response: Callable[[HttpRequest], HttpResponse]):
    self.get_response = get_response
    self.key = settings.JWT_SECRET.encode("utf-8")
    if len(self.key) < 32 or not settings.JWT_ISSUER:
      raise ImproperlyConfigured("Set JWT_SECRET (at least 32 UTF-8 bytes) and JWT_ISSUER to match the backend.")

  def __call__(self, request: HttpRequest) -> HttpResponse:
    # JWT auth applies only to the PDF API; Djanog admin keeps its session login.
    # print("Hello world ana si middleware ;DDDDDDDDDDDDDDD")
    if request.path_info != "/api/pdf-parser" and not request.path_info.startswith("/api/pdf-parser/"):
      return self.get_response(request)

    try:
      scheme, token = request.headers.get("Authorization", "").split()
      if scheme.lower() != "bearer":
        raise ValueError
      claims = jwt.decode(token, self.key, algorithms=[settings.JWT_ALGORITHM],
                          issuer=settings.JWT_ISSUER, options={"require": ["exp", "iss", "sub", "role"]})
      user_id = int(claims["sub"])
      role = claims["role"]
      if user_id <= 0 or not isinstance(role, str) or not role.strip():
        raise ValueError
    except (jwt.InvalidTokenError, ValueError, TypeError):
      return JsonResponse({"message": "Invalid or missing authentication token."}, status=401,
                          headers={"WWW-Authenticate": "Bearer"})

    request.uesr_id = user_id
    request.user_role = role
    return self.get_response(request)

  