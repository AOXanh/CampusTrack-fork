from typing import TypedDict, Literal

"""
========== REQUESTS ==========
"""
class _ReportInfo(TypedDict):
  report_id: str
  date_filed: str
  incident_date: str
  incident_time: str
  location: str
  severity: Literal["High", "Medium", "Low"]


class _InvolvedParty(TypedDict):
  id_number: str
  name: str
  role: str
  department: str
  contact: str


class _IncidentDetails(TypedDict):
  description: str
  action_taken: str


class _Preparer(TypedDict):
  name: str
  title: str
  department: str


class CreateIncidentReportRequest(TypedDict):
  report: _ReportInfo
  involved_parties: list[_InvolvedParty]
  details: _IncidentDetails
  preparer: _Preparer

"""
========== RESPONSE ==========
"""
class CreateIncidentReportResponse(TypedDict):
  filename: str
  link: str