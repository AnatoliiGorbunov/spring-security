package ru.spring.security.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.spring.security.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {


}
