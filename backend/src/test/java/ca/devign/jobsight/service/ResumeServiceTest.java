package ca.devign.jobsight.service;

import ca.devign.jobsight.ml.client.PythonMlClient;
import ca.devign.jobsight.ml.dto.MatchResult;
import ca.devign.jobsight.model.Resume;
import ca.devign.jobsight.model.User;
import ca.devign.jobsight.repository.ResumeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ResumeServiceTest {

    @Mock
    private ResumeRepository resumeRepository;

    @Mock
    private PythonMlClient mlClient;

    @InjectMocks
    private ResumeService resumeService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldUploadResumeAndReturnMatchResult() throws IOException {
        // Arrange
        User user = new User();
        MockMultipartFile file = new MockMultipartFile(
                "resume", "test.pdf", "application/pdf", "Dummy PDF".getBytes()
        );
        String jd = "Looking for a developer with Python and AWS skills.";
        String resumeText = "Experienced in Python and Java";

        MatchResult mockResult = new MatchResult(85, List.of("AWS"));
        when(mlClient.extractText(any(), anyString())).thenReturn(resumeText);
        when(mlClient.match(resumeText, jd)).thenReturn(mockResult);

        // Act
        MatchResult result = resumeService.uploadResumeAndMatch(file, user, jd);

        // Assert
        assertEquals(85, result.getScore());
        assertTrue(result.getMissingSkills().contains("AWS"));
        verify(resumeRepository, times(1)).save(any(Resume.class));
    }

    @Test
    void shouldRejectInvalidFileType() {
        User user = new User();
        MockMultipartFile file = new MockMultipartFile("resume", "bad.exe", "application/octet-stream", "binary".getBytes());

        assertThrows(IllegalArgumentException.class, () ->
            resumeService.uploadResumeAndMatch(file, user, "irrelevant JD"));
    }

    @Test
    void shouldReturnUserResumes() {
        User user = new User();
        List<Resume> mockResumes = List.of(new Resume(), new Resume());

        when(resumeRepository.findByUser(user)).thenReturn(mockResumes);

        List<Resume> resumes = resumeService.getUserResumes(user);

        assertEquals(2, resumes.size());
        verify(resumeRepository, times(1)).findByUser(user);
    }
}