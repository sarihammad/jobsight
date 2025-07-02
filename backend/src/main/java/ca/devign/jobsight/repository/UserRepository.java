package ca.devign.jobsight.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ca.devign.jobsight.model.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}