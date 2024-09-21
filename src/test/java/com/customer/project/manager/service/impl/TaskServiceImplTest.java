package com.customer.project.manager.service.impl;

import com.customer.project.manager.domain.Customer;
import com.customer.project.manager.domain.Project;
import com.customer.project.manager.domain.Task;
import com.customer.project.manager.enumeration.TaskStatus;
import com.customer.project.manager.exception.EntityNotFoundException;
import com.customer.project.manager.payload.task.TaskRequest;
import com.customer.project.manager.payload.task.TaskResponse;
import com.customer.project.manager.repository.CustomerRepository;
import com.customer.project.manager.repository.ProjectRepository;
import com.customer.project.manager.repository.TaskRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("TaskServiceImpl Tests")
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private TaskRequest taskRequest;
    private Task task;
    private Project project;
    private Customer customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        project = Project.builder()
                .id(UUID.randomUUID())
                .name("Project 1")
                .build();

        customer = Customer.builder()
                .id(UUID.randomUUID())
                .name("John Doe")
                .email("john.doe@example.com")
                .build();

        taskRequest = new TaskRequest();
        taskRequest.setTitle("Task 1");
        taskRequest.setDescription("Task Description");
        taskRequest.setStatus(TaskStatus.TO_DO);
        taskRequest.setProjectId(project.getId());
        taskRequest.setCustomerId(customer.getId());

        task = Task.builder()
                .id(UUID.randomUUID())
                .title("Task 1")
                .description("Task Description")
                .status(TaskStatus.TO_DO)
                .project(project)
                .customer(customer)
                .build();
    }

    @Test
    @DisplayName("Should create and return task response")
    void testCreateTask() {
        when(projectRepository.findById(taskRequest.getProjectId())).thenReturn(Optional.of(project));
        when(customerRepository.findById(taskRequest.getCustomerId())).thenReturn(Optional.of(customer));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskResponse response = taskService.create(taskRequest);

        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo(taskRequest.getTitle());
        assertThat(response.getDescription()).isEqualTo(taskRequest.getDescription());

        verify(projectRepository).findById(taskRequest.getProjectId());
        verify(customerRepository).findById(taskRequest.getCustomerId());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    @DisplayName("Should create task with customer when CustomerId is present")
    void testCreateTaskWithCustomer() {
        taskRequest.setCustomerId(customer.getId());

        when(projectRepository.findById(taskRequest.getProjectId())).thenReturn(Optional.of(project));
        when(customerRepository.findById(taskRequest.getCustomerId())).thenReturn(Optional.of(customer));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskResponse response = taskService.create(taskRequest);

        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo(taskRequest.getTitle());

        verify(projectRepository).findById(taskRequest.getProjectId());
        verify(customerRepository).findById(taskRequest.getCustomerId());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    @DisplayName("Should create task without customer when CustomerId is null")
    void testCreateTaskWithoutCustomer() {
        taskRequest.setCustomerId(null);

        when(projectRepository.findById(taskRequest.getProjectId())).thenReturn(Optional.of(project));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskResponse response = taskService.create(taskRequest);

        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo(taskRequest.getTitle());

        verify(projectRepository).findById(taskRequest.getProjectId());
        verify(customerRepository, never()).findById(any(UUID.class));
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when project not found on task creation")
    void testCreateTaskProjectNotFound() {
        when(projectRepository.findById(taskRequest.getProjectId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.create(taskRequest))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Project not found with id: " + taskRequest.getProjectId());

        verify(projectRepository).findById(taskRequest.getProjectId());
        verify(customerRepository, never()).findById(any(UUID.class));
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when customer not found on task creation")
    void testCreateTaskCustomerNotFound() {
        when(projectRepository.findById(taskRequest.getProjectId())).thenReturn(Optional.of(project));
        when(customerRepository.findById(taskRequest.getCustomerId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.create(taskRequest))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Customer not found with id: " + taskRequest.getCustomerId());

        verify(projectRepository).findById(taskRequest.getProjectId());
        verify(customerRepository).findById(taskRequest.getCustomerId());
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    @DisplayName("Should update and return task response")
    void testUpdateTask() {
        UUID taskId = task.getId();
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(projectRepository.findById(taskRequest.getProjectId())).thenReturn(Optional.of(project));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskResponse response = taskService.update(taskId, taskRequest);

        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo(taskRequest.getTitle());
        assertThat(response.getDescription()).isEqualTo(taskRequest.getDescription());

        verify(taskRepository).findById(taskId);
        verify(projectRepository).findById(taskRequest.getProjectId());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when task not found on update")
    void testUpdateTaskNotFound() {
        UUID taskId = UUID.randomUUID();
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.update(taskId, taskRequest))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Task not found with id: " + taskId);

        verify(taskRepository).findById(taskId);
        verify(projectRepository, never()).findById(any(UUID.class));
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when project not found on task update")
    void testUpdateTaskProjectNotFound() {
        UUID taskId = task.getId();
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(projectRepository.findById(taskRequest.getProjectId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.update(taskId, taskRequest))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Project not found with id: " + taskRequest.getProjectId());

        verify(taskRepository).findById(taskId);
        verify(projectRepository).findById(taskRequest.getProjectId());
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    @DisplayName("Should update task with customer")
    void testUpdateTaskCustomer() {
        UUID taskId = task.getId();
        UUID customerId = customer.getId();
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskResponse response = taskService.update(taskId, customerId);

        assertThat(response).isNotNull();

        verify(customerRepository).findById(customerId);
        verify(taskRepository).findById(taskId);
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when customer not found on task update")
    void testUpdateTaskCustomerNotFound() {
        UUID taskId = task.getId();
        UUID customerId = UUID.randomUUID();
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.update(taskId, customerId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Customer not found with id: " + customerId);

        verify(customerRepository).findById(customerId);
        verify(taskRepository, never()).findById(any(UUID.class));
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when task not found on task update with customer")
    void testUpdateTaskCustomerTaskNotFound() {
        UUID taskId = UUID.randomUUID();
        UUID customerId = customer.getId();
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.update(taskId, customerId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Task not found with id: " + taskId);

        verify(customerRepository).findById(customerId);
        verify(taskRepository).findById(taskId);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    @DisplayName("Should find and return task by ID")
    void testFindById() {
        UUID taskId = task.getId();
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        TaskResponse response = taskService.findById(taskId);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(taskId);

        verify(taskRepository).findById(taskId);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when task not found by ID")
    void testFindByIdNotFound() {
        UUID taskId = UUID.randomUUID();
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.findById(taskId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Task not found with id: " + taskId);

        verify(taskRepository).findById(taskId);
    }

    @Test
    @DisplayName("Should return paginated tasks")
    void testFindAll() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Task> taskPage = new PageImpl<>(List.of(task));
        when(taskRepository.findAll(pageable)).thenReturn(taskPage);

        Page<TaskResponse> responsePage = taskService.findAll(pageable);

        assertThat(responsePage).isNotEmpty();
        assertThat(responsePage.getContent()).hasSize(1);
        assertThat(responsePage.getContent().get(0).getTitle()).isEqualTo(task.getTitle());

        verify(taskRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Should delete the task")
    void testDeleteTask() {
        UUID taskId = task.getId();
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        taskService.delete(taskId);

        verify(taskRepository).findById(taskId);
        verify(taskRepository).delete(task);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when task not found on delete")
    void testDeleteTaskNotFound() {
        UUID taskId = UUID.randomUUID();
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.delete(taskId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Task not found with id: " + taskId);

        verify(taskRepository).findById(taskId);
        verify(taskRepository, never()).delete(any(Task.class));
    }
}
