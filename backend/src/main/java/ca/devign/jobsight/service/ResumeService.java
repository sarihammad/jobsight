package ca.devign.jobsight.service;

import ca.devign.jobsight.ml.client.PythonMlClient;
import ca.devign.jobsight.ml.dto.MatchResult;
import ca.devign.jobsight.model.Resume;
import ca.devign.jobsight.model.User;
import ca.devign.jobsight.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final PythonMlClient mlClient;

    private static final String UPLOAD_DIR = "uploads";

    public MatchResult uploadResumeAndMatch(MultipartFile file, User user, String jobDescription) throws IOException {
        validateFileType(file.getOriginalFilename());

        Files.createDirectories(Paths.get(UPLOAD_DIR));
        Path path = Paths.get(UPLOAD_DIR, UUID.randomUUID() + "_" + file.getOriginalFilename());
        Files.write(path, file.getBytes());

        Resume resume = new Resume();
        resume.setOriginalFilename(file.getOriginalFilename());
        resume.setFilePath(path.toString());
        resume.setUser(user);
        resumeRepository.save(resume);

        String resumeText = mlClient.extractText(file.getBytes(), file.getOriginalFilename());
        return mlClient.match(resumeText, jobDescription);
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