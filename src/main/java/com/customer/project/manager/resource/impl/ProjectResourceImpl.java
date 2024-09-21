package com.customer.project.manager.resource.impl;

import com.customer.project.manager.payload.project.ProjectRequest;
import com.customer.project.manager.payload.project.ProjectResponse;
import com.customer.project.manager.resource.ProjectResource;
import com.customer.project.manager.service.ProjectService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectResourceImpl implements ProjectResource {

    private final ProjectService projectService;

    @Override
    @PostMapping
    public ResponseEntity<ProjectResponse> create(@RequestBody ProjectRequest request) {
        ProjectResponse response = projectService.create(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse> update(UUID id, ProjectRequest request) {
        ProjectResponse response = projectService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> findById(@PathVariable UUID id) {
        ProjectResponse response = projectService.findById(id);
        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping
    public ResponseEntity<Page<ProjectResponse>> findAll(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "15") int pageSize
    ) {
        Page<ProjectResponse> response = projectService.findAll(Pageable.ofSize(pageSize).withPage(page));
        return ResponseEntity.ok(response);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        projectService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
