package ca.devign.jobsight.ml.client;

import ca.devign.jobsight.ml.dto.MatchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class PythonMlClient {

    private final RestClient restClient;

    @Value("${ml.service.url}")
    private String mlServiceUrl;

    public String extractText(byte[] fileBytes, String filename) {
        var resource = new ByteArrayResource(fileBytes) {
            @Override
            public String getFilename() {
                return filename;
            }
        };

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", resource);

        return restClient.post()
                .uri(mlServiceUrl + "/parse-resume")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(builder.build())
                .retrieve()
                .body(String.class);
    }

    public MatchResult match(String resumeText, String jobDescription) {
        var payload = new HashMap<String, String>();
        payload.put("resume", resumeText);
        payload.put("job", jobDescription);

        return restClient.post()
                .uri(mlServiceUrl + "/match")
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload)
                .retrieve()
                .body(MatchResult.class);
    }
}
