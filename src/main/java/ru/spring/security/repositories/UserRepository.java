package ru.spring.security.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.spring.security.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
}
