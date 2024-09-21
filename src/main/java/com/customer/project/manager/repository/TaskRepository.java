package com.customer.project.manager.repository;

import com.customer.project.manager.domain.Task;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    Optional<Task> findByTitle(String title);
}
