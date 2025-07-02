package ca.devign.jobsight.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Resume {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalFilename;
    private String filePath;
    private LocalDateTime uploadedAt = LocalDateTime.now();

    @ManyToOne
    private User user;

    
}