import io
import pytest
from fastapi.testclient import TestClient
from app.main import app
from reportlab.pdfgen import canvas
from io import BytesIO

client = TestClient(app)

def generate_minimal_pdf():
    buffer = BytesIO()
    c = canvas.Canvas(buffer)
    skills_text = "Python, Docker, Leadership, Communication"
    c.drawString(100, 750, skills_text)
    c.save()
    buffer.seek(0)
    return buffer.getvalue()

def test_parse_resume_returns_text():
    pdf_bytes = generate_minimal_pdf()
    response = client.post("/parse-resume", files={"file": ("resume.pdf", pdf_bytes, "application/pdf")})
    assert response.status_code == 200
    assert "text" in response.json()
    text = response.json()["text"]
    assert "Python" in text or "Docker" in text or "Leadership" in text or "Communication" in text

def test_match_resume():
    response = client.post("/match", json={
        "resume": "I have experience with Python, Docker, and Leadership.",
        "job": "We require AWS, Docker, Kubernetes, and Python for a cloud engineer role."
    })
    assert response.status_code == 200
    data = response.json()
    assert "score" in data
    assert "missingSkills" in data
    print(f"Missing Skills: {data['missingSkills']}")
    assert "aws" in [skill.lower() for skill in data["missingSkills"]]

def test_match_handles_empty_resume():
    payload = {"resume": "", "job": "Python"}
    response = client.post("/match", json=payload)
    assert response.status_code == 422

def test_parse_resume_invalid_file():
    response = client.post(
        "/parse-resume",
        files={"file": ("resume.txt", b"not a PDF", "text/plain")}
    )
    assert response.status_code == 400