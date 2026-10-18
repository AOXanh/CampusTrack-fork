from django.urls import path
from pdf_parser import views

urlpatterns = [
    path("create/", views.create_pdf_report, name="create_pdf_report")
]