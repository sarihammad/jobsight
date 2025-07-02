package ca.devign.jobsight.service;

import ca.devign.jobsight.model.Resume;
import ca.devign.jobsight.model.User;
import ca.devign.jobsight.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;

    private static final String UPLOAD_DIR = "uploads";

    public void uploadResume(MultipartFile file, User user) throws IOException {
        validateFileType(file.getOriginalFilename());

        Path dir = Paths.get(UPLOAD_DIR);
        Files.createDirectories(dir);

        Path path = dir.resolve(UUID.randomUUID() + "_" + file.getOriginalFilename());
        Files.write(path, file.getBytes());

        Resume resume = new Resume();
        resume.setOriginalFilename(file.getOriginalFilename());
        resume.setFilePath(path.toString());
        resume.setUser(user);
        resumeRepository.save(resume);
    }

    public List<Resume> getUserResumes(User user) {
        return resumeRepository.findByUser(user);
    }

    private void validateFileType(String filename) {
        if (filename == null || (!filename.endsWith(".pdf") && !filename.endsWith(".docx"))) {
            throw new IllegalArgumentException("Invalid file type: must be PDF or DOCX");
        }
    }
}