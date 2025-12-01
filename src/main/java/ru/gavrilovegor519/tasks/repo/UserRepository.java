package ru.gavrilovegor519.tasks.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gavrilovegor519.tasks.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}