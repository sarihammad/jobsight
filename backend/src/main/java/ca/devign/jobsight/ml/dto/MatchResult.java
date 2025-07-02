package ca.devign.jobsight.ml.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchResult {
    private int score;
    private List<String> missingSkills;
}