def get_template():
	 
	return """
					<!DOCTYPE html>
					<html lang="en">
					<head>
					    <meta charset="UTF-8">
					    <title>Incident Report - University of Cebu</title>
					    <style>
					        @import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap');
					        body { font-family: 'Inter', sans-serif; color: #1e293b; background-color: #ffffff; font-size: 12px; margin: 0; padding: 40px; line-height: 1.5; }
					        
					        /* Header Section */
					        .header { text-align: center; border-bottom: 2px solid #1e3a8a; padding-bottom: 20px; margin-bottom: 30px; }
					        .header h1 { margin: 0; color: #1e3a8a; font-size: 24px; font-weight: 700; text-transform: uppercase; letter-spacing: 1px; }
					        .header h2 { margin: 5px 0 0 0; color: #334155; font-size: 14px; font-weight: 500; }
					        .header h3 { margin: 15px 0 0 0; color: #0f172a; font-size: 18px; font-weight: 600; text-transform: uppercase; }
					        
					        /* Section Titles */
					        .section-title { background-color: #f1f5f9; color: #1e3a8a; font-weight: 700; padding: 8px 12px; margin: 20px 0 10px 0; border-left: 4px solid #1e3a8a; font-size: 13px; text-transform: uppercase; }
					        
					        /* Grid Tables for Info */
					        .info-table { width: 100%; border-collapse: collapse; margin-bottom: 20px; }
					        .info-table td { border: 1px solid #cbd5e1; padding: 8px 12px; vertical-align: top; }
					        .info-table .label { font-weight: 600; color: #475569; width: 20%; background-color: #f8fafc; font-size: 11px; text-transform: uppercase; }
					        .info-table .value { font-weight: 500; color: #0f172a; width: 30%; }
					        
					        /* List Tables */
					        .list-table { width: 100%; border-collapse: collapse; margin-bottom: 20px; }
					        .list-table th { background-color: #1e3a8a; color: #ffffff; font-weight: 600; font-size: 11px; text-transform: uppercase; padding: 10px 12px; text-align: left; }
					        .list-table td { border: 1px solid #cbd5e1; padding: 10px 12px; font-size: 12px; }
					        .list-table tr:nth-child(even) td { background-color: #f8fafc; }
					        
					        /* Text Areas */
					        .text-block { border: 1px solid #cbd5e1; padding: 15px; border-radius: 4px; background-color: #ffffff; min-height: 80px; white-space: pre-wrap; font-family: 'Inter', sans-serif; color: #334155; }
					        
					        /* Signatories */
					        .signatory-section { margin-top: 50px; display: flex; justify-content: space-between; }
					        .signatory-box { width: 45%; }
					        .signature-line { border-bottom: 1px solid #000; height: 40px; margin-bottom: 8px; }
					        .signatory-name { font-weight: 700; color: #0f172a; text-transform: uppercase; font-size: 13px; }
					        .signatory-title { color: #64748b; font-size: 11px; }
					        
					        /* Badges */
					        .badge { display: inline-block; padding: 3px 8px; border-radius: 12px; font-size: 10px; font-weight: 700; text-transform: uppercase; }
					        .badge-high { background-color: #fee2e2; color: #991b1b; }
					        .badge-medium { background-color: #fef08a; color: #854d0e; }
					        .badge-low { background-color: #dcfce7; color: #166534; }
					    </style>
					</head>
					<body>
					    <div class="header">
					        <h1>University of Cebu</h1>
					        <h2>Student Affairs Office / Security Department</h2>
					        <h3>Official Incident Report</h3>
					    </div>

					    <div class="section-title">I. General Information</div>
					    <table class="info-table">
					        <tr>
					            <td class="label">Report ID No.</td>
					            <td class="value">{{ report.report_id }}</td>
					            <td class="label">Date Filed</td>
					            <td class="value">{{ report.date_filed }}</td>
					        </tr>
					        <tr>
					            <td class="label">Date of Incident</td>
					            <td class="value">{{ report.incident_date }}</td>
					            <td class="label">Time of Incident</td>
					            <td class="value">{{ report.incident_time }}</td>
					        </tr>
					        <tr>
					            <td class="label">Location</td>
					            <td class="value" colspan="3">{{ report.location }}</td>
					        </tr>
					        <tr>
					            <td class="label">Severity Level</td>
					            <td class="value" colspan="3">
					                <span class="badge badge-{{ report.severity | lower }}">{{ report.severity }}</span>
					            </td>
					        </tr>
					    </table>

					    <div class="section-title">II. Involved Parties</div>
					    <table class="list-table">
					        <thead>
					            <tr>
					                <th>ID Number</th>
					                <th>Full Name</th>
					                <th>Role/Affiliation</th>
					                <th>Department/Course</th>
					                <th>Contact No.</th>
					            </tr>
					        </thead>
					        <tbody>
					            {% for party in involved_parties %}
					            <tr>
					                <td>{{ party.id_number }}</td>
					                <td style="font-weight: 600;">{{ party.name }}</td>
					                <td>{{ party.role }}</td>
					                <td>{{ party.department }}</td>
					                <td>{{ party.contact }}</td>
					            </tr>
					            {% else %}
					            <tr>
					                <td colspan="5" style="text-align: center; font-style: italic;">No parties recorded.</td>
					            </tr>
					            {% endfor %}
					        </tbody>
					    </table>

					    <div class="section-title">III. Incident Description</div>
					    <div class="text-block">{{ details.description }}</div>

					    <div class="section-title">IV. Immediate Action Taken</div>
					    <div class="text-block">{{ details.action_taken }}</div>

					    <div class="signatory-section">
					        <div class="signatory-box">
					            <div class="signature-line"></div>
					            <div class="signatory-name">{{ preparer.name }}</div>
					            <div class="signatory-title">{{ preparer.title }}</div>
					            <div class="signatory-title">{{ preparer.department }}</div>
					        </div>
					        <div class="signatory-box">
					            <div class="signature-line"></div>
					            <div class="signatory-name">Noted By (Head/Director)</div>
					            <div class="signatory-title">Signature over printed name</div>
					            <div class="signatory-title">Date: __________________</div>
					        </div>
					    </div>
					</body>
					</html>
				"""