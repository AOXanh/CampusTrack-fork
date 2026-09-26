from django.urls import path
from . import views

urlpatterns = [
    path("", views.pdf_parser_health, name="pdf_parser_helloworld"),
    path("health/", views.pdf_parser_health, name="pdf_parser_health"),
]