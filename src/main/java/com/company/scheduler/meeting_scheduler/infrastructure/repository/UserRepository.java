package com.company.scheduler.meeting_scheduler.infrastructure.repository;

import com.company.scheduler.meeting_scheduler.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    // Aquí puedes agregar métodos personalizados si los necesitas, por ejemplo:
    // List<User> findByTimezone(String timezone);
}