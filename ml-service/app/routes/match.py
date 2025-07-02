from fastapi import APIRouter
from models.schemas import MatchPayload
from services.matcher import match_resume_to_job

router = APIRouter()

@router.post("/match")
async def match_resume(payload: MatchPayload):
    return match_resume_to_job(payload)