from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity
from services.skills import extract_skills
import logging

logger = logging.getLogger(__name__)

def match_resume_to_job(payload):
    logger.info("Matching resume with job description.")

    tfidf = TfidfVectorizer().fit([payload.resume, payload.job])
    vecs = tfidf.transform([payload.resume, payload.job])
    score = cosine_similarity(vecs[0], vecs[1])[0][0]

    resume_skills = extract_skills(payload.resume)
    job_skills = extract_skills(payload.job)

    print(f"Job Skills: {job_skills}")
    print(f"Resume Skills: {resume_skills}")

    missing = sorted(job_skills - resume_skills)

    logger.info(f"Score: {score:.3f} | Missing: {missing}")

    return {"score": round(score * 100), "missingSkills": missing}