package com.customer.project.manager.service.impl;

import com.customer.project.manager.domain.Customer;
import com.customer.project.manager.domain.Project;
import com.customer.project.manager.domain.Task;
import com.customer.project.manager.exception.EntityNotFoundException;
import com.customer.project.manager.payload.task.TaskRequest;
import com.customer.project.manager.payload.task.TaskResponse;
import com.customer.project.manager.payload.task.convert.TaskConverter;
import com.customer.project.manager.repository.CustomerRepository;
import com.customer.project.manager.repository.ProjectRepository;
import com.customer.project.manager.repository.TaskRepository;
import com.customer.project.manager.service.TaskService;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import static java.util.Objects.nonNull;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskServiceImpl.class);
    private final TaskRepository taskRepository;
    private final CustomerRepository customerRepository;
    private final ProjectRepository projectRepository;

    @Override
    public TaskResponse create(TaskRequest request) {
        LOGGER.info("class=TaskService, method=create, message=Creating a new task");
        Task task;

        Project project = this.projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + request.getProjectId()));

        if (nonNull(request.getCustomerId())) {
            Customer customer = this.customerRepository.findById(request.getCustomerId())
                    .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + request.getCustomerId()));

            task = TaskConverter.convert(request, project, customer);
        } else {
            task = TaskConverter.convert(request, project);
        }

        task = this.taskRepository.save(task);
        LOGGER.info("class=TaskService, method=create, message=Task created with id: {}", task.getId());

        return TaskConverter.convert(task);
    }

    @Override
    public TaskResponse update(UUID id, TaskRequest request) {
        LOGGER.info("class=TaskService, method=update, message=Updating task with id: {}", id);
        Task task = this.taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));

        Project project = this.projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + request.getProjectId()));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setProject(project);

        task = this.taskRepository.save(task);
        LOGGER.info("class=TaskService, method=update, message=Task updated with id: {}", task.getId());

        return TaskConverter.convert(task);
    }

    @Override
    public TaskResponse update(UUID id, UUID customerId) {
        Customer customer = this.customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + customerId));

        Task task = this.taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));

        task.setCustomer(customer);
        task = this.taskRepository.save(task);
        LOGGER.info("class=TaskService, method=update, message=Task updated with id: {}, and CustomerId: {}", task.getId(), customerId);
        return TaskConverter.convert(task);
    }

    @Override
    public TaskResponse findById(UUID id) {
        LOGGER.info("class=TaskService, method=findById, message=Finding task with id: {}", id);
        Task task = this.taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));
        LOGGER.info("class=TaskService, method=findById, message=Task found with id: {}", task.getId());

        return TaskConverter.convert(task);
    }

    @Override
    public Page<TaskResponse> findAll(Pageable pageable) {
        LOGGER.info("class=TaskService, method=findAll, message=Finding all tasks");
        return this.taskRepository.findAll(pageable)
                .map(TaskConverter::convert);
    }

    @Override
    public void delete(UUID id) {
        LOGGER.info("class=TaskService, method=delete, message=Deleting task with id: {}", id);
        Task task = this.taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));
        this.taskRepository.delete(task);
        LOGGER.info("class=TaskService, method=delete, message=Task deleted with id: {}", id);
    }
}
