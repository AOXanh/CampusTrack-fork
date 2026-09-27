from django.urls import path
from . import views

urlpatterns = [
    path("", views.pdf_parser_helloworld, name="pdf_parser_helloworld"),
    path("create/", views.create_pdf_report, name="create_pdf_report")
]