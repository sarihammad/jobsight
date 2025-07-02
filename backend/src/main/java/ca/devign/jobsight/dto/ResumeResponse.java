package ca.devign.jobsight.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ResumeResponse {
    public String filename;
    public LocalDateTime uploadedAt;
    public int matchScore;
    public List<String> missingSkills;

    public ResumeResponse(String filename, LocalDateTime uploadedAt, int matchScore, List<String> missingSkills) {
        this.filename = filename;
        this.uploadedAt = uploadedAt;
        this.matchScore = matchScore;
        this.missingSkills = missingSkills;
    }
}