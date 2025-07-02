package ca.devign.jobsight.service;

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

    @InjectMocks
    private ResumeService resumeService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldUploadValidResume() throws IOException {
        User user = new User();
        MockMultipartFile file = new MockMultipartFile("resume", "test.pdf", "application/pdf", "Hello".getBytes());

        resumeService.uploadResume(file, user);

        verify(resumeRepository, times(1)).save(any(Resume.class));
    }

    @Test
    void shouldRejectInvalidFileExtension() {
        User user = new User();
        MockMultipartFile file = new MockMultipartFile("resume", "test.txt", "text/plain", "Hello".getBytes());

        assertThrows(IllegalArgumentException.class, () -> {
            resumeService.uploadResume(file, user);
        });
    }

    @Test
    void shouldReturnUserResumes() {
        User user = new User();
        List<Resume> mockList = List.of(new Resume(), new Resume());

        when(resumeRepository.findByUser(user)).thenReturn(mockList);

        List<Resume> result = resumeService.getUserResumes(user);

        assertEquals(2, result.size());
        verify(resumeRepository, times(1)).findByUser(user);
    }
}