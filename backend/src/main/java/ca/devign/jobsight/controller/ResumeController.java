package ca.devign.jobsight.controller;

import ca.devign.jobsight.dto.ResumeResponse;
import ca.devign.jobsight.model.User;
import ca.devign.jobsight.model.Resume;
import ca.devign.jobsight.service.ResumeService;
import ca.devign.jobsight.service.UserService;
import lombok.RequiredArgsConstructor;
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
// import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("/api/resumes")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;
    private final UserService userService;

    @PostMapping("/upload")
    public String uploadResume(@RequestParam MultipartFile file, Principal principal) throws Exception {
        User user = userService.getFullUserByEmail(principal.getName());
        resumeService.uploadResume(file, user);
        return "Resume uploaded successfully.";
    }

    @GetMapping
    public List<ResumeResponse> listResumes(Principal principal) {
        User user = userService.getFullUserByEmail(principal.getName());
        List<Resume> resumes = resumeService.getUserResumes(user);

        return resumes.stream().map(r -> new ResumeResponse(
                r.getOriginalFilename(),
                r.getUploadedAt(),
                new Random().nextInt(100), // mock match score
                List.of("AWS", "Docker")   // mock skill gaps
        )).toList();
    }
}