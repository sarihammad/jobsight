from fastapi import APIRouter, UploadFile, File
from app.services.parser import extract_text_from_pdf

router = APIRouter()

@router.post("/parse-resume")
async def parse_resume(file: UploadFile = File(...)):
    text = await extract_text_from_pdf(file)
    return {"text": text}