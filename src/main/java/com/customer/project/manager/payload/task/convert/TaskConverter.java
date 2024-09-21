package com.customer.project.manager.payload.task.convert;

import com.customer.project.manager.domain.Customer;
import com.customer.project.manager.domain.Project;
import com.customer.project.manager.domain.Task;
import com.customer.project.manager.payload.task.TaskRequest;
import com.customer.project.manager.payload.task.TaskResponse;

public class TaskConverter {
    public static Task convert(TaskRequest request, Project project) {
        return Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus())
                .project(project)
                .build();
    }

    public static Task convert(TaskRequest request, Project project, Customer customer) {
        return Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus())
                .customer(customer)
                .project(project)
                .build();
    }

    public static TaskResponse convert(Task task) {
        return new TaskResponse()
                .setId(task.getId())
                .setTitle(task.getTitle())
                .setDescription(task.getDescription())
                .setStatus(task.getStatus())
                .setProjectId(task.getProject().getId());
    }
}
