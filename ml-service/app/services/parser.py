import fitz
import logging

logger = logging.getLogger(__name__)

async def extract_text_from_pdf(file):
    try:
        content = await file.read()
        doc = fitz.open(stream=content, filetype="pdf")
        return " ".join(page.get_text() for page in doc)
    except Exception as e:
        logger.exception("Failed to parse resume PDF")
        raise RuntimeError("Resume parsing failed")