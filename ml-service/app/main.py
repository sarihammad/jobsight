from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.routes import resume, match
from app.exceptions.handlers import general_exception_handler, validation_exception_handler, runtime_exception_handler
from fastapi.exceptions import RequestValidationError

app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"], 
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

app.include_router(resume.router)
app.include_router(match.router)

#Exception handlers
app.add_exception_handler(Exception, general_exception_handler)
app.add_exception_handler(RequestValidationError, validation_exception_handler)
app.add_exception_handler(RuntimeError, runtime_exception_handler)

if __name__ == "__main__":
    import uvicorn
    uvicorn.run("main:app", host="0.0.0.0", port=5001, reload=True)