package com.customer.project.manager.payload.project.convert;

import com.customer.project.manager.domain.Customer;
import com.customer.project.manager.domain.Project;
import com.customer.project.manager.payload.project.ProjectRequest;
import com.customer.project.manager.payload.project.ProjectResponse;

public class ProjectConverter {
    public static Project convert(ProjectRequest request, Customer customer) {
        return Project.builder()
                .name(request.getName())
                .description(request.getDescription())
                .status(request.getStatus())
                .customer(customer)
                .build();
    }

    public static ProjectResponse convert(Project project) {
        return new ProjectResponse().setId(project.getId())
                .setName(project.getName())
                .setDescription(project.getDescription())
                .setStatus(project.getStatus())
                .setCustomerId(project.getCustomer().getId());
    }
}

