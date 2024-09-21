package com.customer.project.manager.service.impl;

import com.customer.project.manager.domain.Customer;
import com.customer.project.manager.domain.Project;
import com.customer.project.manager.exception.EntityNotFoundException;
import com.customer.project.manager.payload.project.ProjectRequest;
import com.customer.project.manager.payload.project.ProjectResponse;
import com.customer.project.manager.payload.project.convert.ProjectConverter;
import com.customer.project.manager.repository.CustomerRepository;
import com.customer.project.manager.repository.ProjectRepository;
import com.customer.project.manager.service.ProjectService;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectServiceImpl.class);
    private final ProjectRepository projectRepository;
    private final CustomerRepository customerRepository;

    @Override
    public ProjectResponse create(ProjectRequest request) {
        LOGGER.info("class=ProjectService, method=create, message=Creating a new project");
        Customer customer = this.customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + request.getCustomerId()));

        Project project = ProjectConverter.convert(request, customer);
        project = this.projectRepository.save(project);
        LOGGER.info("class=ProjectService, method=create, message=Project created with id: {}", project.getId());

        return ProjectConverter.convert(project);
    }

    @Override
    public ProjectResponse update(UUID id, ProjectRequest request) {
        LOGGER.info("class=ProjectService, method=update, message=Updating project with id: {}", id);
        Project project = this.projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + id));

        Customer customer = this.customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + request.getCustomerId()));

        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setStatus(request.getStatus());
        project.setCustomer(customer);

        project = this.projectRepository.save(project);
        LOGGER.info("class=ProjectService, method=update, message=Project updated with id: {}", project.getId());

        return ProjectConverter.convert(project);
    }

    @Override
    public ProjectResponse findById(UUID id) {
        LOGGER.info("class=ProjectService, method=findById, message=Finding project with id: {}", id);
        Project project = this.projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + id));
        LOGGER.info("class=ProjectService, method=findById, message=Project found with id: {}", project.getId());

        return ProjectConverter.convert(project);
    }

    @Override
    public Page<ProjectResponse> findAll(Pageable pageable) {
        LOGGER.info("class=ProjectService, method=findAll, message=Finding all projects");
        return this.projectRepository.findAll(pageable)
                .map(ProjectConverter::convert);
    }

    @Override
    public void delete(UUID id) {
        LOGGER.info("class=ProjectService, method=delete, message=Deleting project with id: {}", id);
        Project project = this.projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + id));
        this.projectRepository.delete(project);
        LOGGER.info("class=ProjectService, method=delete, message=Project deleted with id: {}", id);
    }
}

