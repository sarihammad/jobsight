from fastapi import Request
from fastapi.responses import JSONResponse
from fastapi.exceptions import RequestValidationError
import logging

logger = logging.getLogger(__name__)

async def general_exception_handler(request: Request, exc: Exception):
    logger.exception("Unhandled exception")
    return JSONResponse(status_code=500, content={"error": str(exc)})

async def validation_exception_handler(request: Request, exc: RequestValidationError):
    logger.warning("Validation failed")
    errors = exc.errors()
    for err in errors:
        if "ctx" in err and isinstance(err["ctx"].get("error"), Exception):
            err["ctx"]["error"] = str(err["ctx"]["error"])
    return JSONResponse(status_code=422, content={"detail": errors})

async def runtime_exception_handler(request: Request, exc: RuntimeError):
    logger.error("Runtime exception", exc_info=True)
    return JSONResponse(status_code=400, content={"detail": str(exc)})