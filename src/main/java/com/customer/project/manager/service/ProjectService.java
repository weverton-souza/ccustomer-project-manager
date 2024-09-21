package com.customer.project.manager.service;

import com.customer.project.manager.payload.project.ProjectRequest;
import com.customer.project.manager.payload.project.ProjectResponse;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectService {
    ProjectResponse create(ProjectRequest request);
    ProjectResponse update(UUID id, ProjectRequest request);
    ProjectResponse findById(UUID id);
    Page<ProjectResponse> findAll(Pageable pageable);
    void delete(UUID id);
}
