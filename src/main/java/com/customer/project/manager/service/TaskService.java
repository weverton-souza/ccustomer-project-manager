package com.customer.project.manager.service;

import com.customer.project.manager.payload.task.TaskRequest;
import com.customer.project.manager.payload.task.TaskResponse;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {
    TaskResponse create(TaskRequest request);
    TaskResponse update(UUID id, TaskRequest request);
    TaskResponse update(UUID id, UUID customerId);
    TaskResponse findById(UUID id);
    Page<TaskResponse> findAll(Pageable pageable);
    void delete(UUID id);
}
