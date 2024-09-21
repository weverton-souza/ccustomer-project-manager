package com.customer.project.manager.resource.impl;

import com.customer.project.manager.payload.task.TaskRequest;
import com.customer.project.manager.payload.task.TaskResponse;
import com.customer.project.manager.resource.TaskResource;
import com.customer.project.manager.service.TaskService;
import java.util.UUID;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskResourceImpl implements TaskResource {

    private final TaskService taskService;

    @Override
    @PostMapping
    public ResponseEntity<TaskResponse> create(@RequestBody TaskRequest request) {
        TaskResponse response = taskService.create(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> update(@PathVariable UUID id, @RequestBody TaskRequest request) {
        TaskResponse response = taskService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/customers/{customerId}")
    public ResponseEntity<TaskResponse> update(@PathVariable UUID id, @PathVariable UUID customerId) {
        TaskResponse response = taskService.update(id, customerId);
        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> findById(@PathVariable UUID id) {
        TaskResponse response = taskService.findById(id);
        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping
    public ResponseEntity<Page<TaskResponse>> findAll(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "15") int pageSize
    ) {
        Page<TaskResponse> response = taskService.findAll(Pageable.ofSize(pageSize).withPage(page));
        return ResponseEntity.ok(response);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
