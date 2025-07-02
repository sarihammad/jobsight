package ca.devign.jobsight.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.*;
import org.springframework.mock.web.MockMultipartFile;

import java.util.HashMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FullIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private String buildRegisterPayload(String email, String password, String fullName) throws Exception {
        var payload = new HashMap<String, String>();
        payload.put("email", email);
        payload.put("password", password);
        payload.put("fullName", fullName);
        return objectMapper.writeValueAsString(payload);
    }

    private String buildLoginPayload(String email, String password) throws Exception {
        var payload = new HashMap<String, String>();
        payload.put("email", email);
        payload.put("password", password);
        return objectMapper.writeValueAsString(payload);
    }

    @Test
    void fullFlow_shouldUploadResumeAfterLogin() throws Exception {
        //Register
        String randomEmail = "user" + System.currentTimeMillis() + "@example.com";
        String registerPayload = buildRegisterPayload(randomEmail, "securepass", "Test User");

        mockMvc.perform(post("/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(registerPayload))
            .andExpect(status().isOk());

        //Login & get token
        String loginPayload = buildLoginPayload(randomEmail, "securepass");

        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginPayload))
                .andExpect(status().isOk())
                .andReturn();

        String response = loginResult.getResponse().getContentAsString();
        String token = response.replace("{\"token\":\"", "").replace("\"}", "");

        //Upload resume with token
        MockMultipartFile file = new MockMultipartFile("file", "resume.pdf", "application/pdf", "PDF content".getBytes());

        mockMvc.perform(multipart("/api/resumes/upload")
                .file(file)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("Resume uploaded successfully."));
    }

    @Test
    void shouldRejectResumeUploadWithoutAuth() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "resume.pdf", "application/pdf", "PDF content".getBytes());

        mockMvc.perform(multipart("/api/resumes/upload").file(file))
                .andExpect(status().isUnauthorized());
    }
}