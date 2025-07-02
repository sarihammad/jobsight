from pydantic import BaseModel, model_validator

class MatchPayload(BaseModel):
    resume: str
    job: str

    @model_validator(mode="after")
    def check_required_fields(self) -> "MatchPayload":
        if not self.resume or not self.job:
            raise ValueError("Both resume and job description are required.")
        return self