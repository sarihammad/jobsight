package ca.devign.jobsight.repository;

import ca.devign.jobsight.model.Resume;
import ca.devign.jobsight.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
    List<Resume> findByUser(User user);
}