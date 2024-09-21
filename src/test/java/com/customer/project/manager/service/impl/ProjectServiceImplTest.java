package com.customer.project.manager.service.impl;

import com.customer.project.manager.domain.Customer;
import com.customer.project.manager.domain.Project;
import com.customer.project.manager.enumeration.ProjectStatus;
import com.customer.project.manager.exception.EntityNotFoundException;
import com.customer.project.manager.payload.project.ProjectRequest;
import com.customer.project.manager.payload.project.ProjectResponse;
import com.customer.project.manager.repository.CustomerRepository;
import com.customer.project.manager.repository.ProjectRepository;
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

@DisplayName("ProjectServiceImpl Tests")
class ProjectServiceImplTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private ProjectServiceImpl projectService;

    private ProjectRequest projectRequest;
    private Customer customer;
    private Project project;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customer = Customer.builder()
                .id(UUID.randomUUID())
                .name("John Doe")
                .email("john.doe@example.com")
                .build();

        projectRequest = new ProjectRequest();
        projectRequest.setName("Project Name");
        projectRequest.setDescription("Project Description");
        projectRequest.setStatus(ProjectStatus.OPEN);
        projectRequest.setCustomerId(customer.getId());

        project = Project.builder()
                .id(UUID.randomUUID())
                .name("Project Name")
                .description("Project Description")
                .status(ProjectStatus.OPEN)
                .customer(customer)
                .build();
    }

    @Test
    @DisplayName("Should create and return project response")
    void testCreateProject() {
        when(customerRepository.findById(projectRequest.getCustomerId())).thenReturn(Optional.of(customer));
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        ProjectResponse response = projectService.create(projectRequest);

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo(projectRequest.getName());
        assertThat(response.getDescription()).isEqualTo(projectRequest.getDescription());

        verify(customerRepository).findById(projectRequest.getCustomerId());
        verify(projectRepository).save(any(Project.class));
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when customer not found on project creation")
    void testCreateProjectCustomerNotFound() {
        when(customerRepository.findById(projectRequest.getCustomerId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> projectService.create(projectRequest))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Customer not found with id: " + projectRequest.getCustomerId());

        verify(customerRepository).findById(projectRequest.getCustomerId());
        verify(projectRepository, never()).save(any(Project.class));
    }

    @Test
    @DisplayName("Should update and return project response")
    void testUpdateProject() {
        UUID projectId = project.getId();
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(customerRepository.findById(projectRequest.getCustomerId())).thenReturn(Optional.of(customer));
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        ProjectResponse response = projectService.update(projectId, projectRequest);

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo(projectRequest.getName());
        assertThat(response.getDescription()).isEqualTo(projectRequest.getDescription());

        verify(projectRepository).findById(projectId);
        verify(customerRepository).findById(projectRequest.getCustomerId());
        verify(projectRepository).save(any(Project.class));
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when project not found on update")
    void testUpdateProjectNotFound() {
        UUID projectId = UUID.randomUUID();
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> projectService.update(projectId, projectRequest))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Project not found with id: " + projectId);

        verify(projectRepository).findById(projectId);
        verify(customerRepository, never()).findById(any(UUID.class));
        verify(projectRepository, never()).save(any(Project.class));
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when customer not found on project update")
    void testUpdateProjectCustomerNotFound() {
        UUID projectId = project.getId();
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(customerRepository.findById(projectRequest.getCustomerId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> projectService.update(projectId, projectRequest))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Customer not found with id: " + projectRequest.getCustomerId());

        verify(projectRepository).findById(projectId);
        verify(customerRepository).findById(projectRequest.getCustomerId());
        verify(projectRepository, never()).save(any(Project.class));
    }

    @Test
    @DisplayName("Should find and return project by ID")
    void testFindById() {
        UUID projectId = project.getId();
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        ProjectResponse response = projectService.findById(projectId);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(projectId);

        verify(projectRepository).findById(projectId);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when project not found by ID")
    void testFindByIdNotFound() {
        UUID projectId = UUID.randomUUID();
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> projectService.findById(projectId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Project not found with id: " + projectId);

        verify(projectRepository).findById(projectId);
    }

    @Test
    @DisplayName("Should return paginated projects")
    void testFindAll() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Project> projectPage = new PageImpl<>(List.of(project));
        when(projectRepository.findAll(pageable)).thenReturn(projectPage);

        Page<ProjectResponse> responsePage = projectService.findAll(pageable);

        assertThat(responsePage).isNotEmpty();
        assertThat(responsePage.getContent()).hasSize(1);
        assertThat(responsePage.getContent().get(0).getName()).isEqualTo(project.getName());

        verify(projectRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Should delete the project")
    void testDeleteProject() {
        UUID projectId = project.getId();
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        projectService.delete(projectId);

        verify(projectRepository).findById(projectId);
        verify(projectRepository).delete(project);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when project not found on delete")
    void testDeleteProjectNotFound() {
        UUID projectId = UUID.randomUUID();
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> projectService.delete(projectId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Project not found with id: " + projectId);

        verify(projectRepository).findById(projectId);
        verify(projectRepository, never()).delete(any(Project.class));
    }
}
