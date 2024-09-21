package com.customer.project.manager.resource.impl;

import com.customer.project.manager.payload.task.TaskRequest;
import com.customer.project.manager.payload.task.TaskResponse;
import com.customer.project.manager.service.TaskService;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("TaskResourceImpl Tests")
class TaskResourceImplTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskResourceImpl taskResource;

    private MockMvc mockMvc;

    private TaskResponse taskResponse;
    private UUID taskId;
    private UUID customerId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(taskResource).build();

        taskId = UUID.randomUUID();
        customerId = UUID.randomUUID();

        taskResponse = new TaskResponse();
        taskResponse.setId(taskId);
        taskResponse.setTitle("Sample Task");
        taskResponse.setDescription("Sample Task Description");
    }

    @Test
    @DisplayName("Should create task and return created task response")
    void testCreateTask() throws Exception {
        when(taskService.create(any(TaskRequest.class))).thenReturn(taskResponse);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "Sample Task",
                                  "description": "Sample Task Description",
                                  "projectId": "c1234567-d89b-42c3-a456-556642440000"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(taskResponse.getId().toString())))
                .andExpect(jsonPath("$.title", is(taskResponse.getTitle())))
                .andExpect(jsonPath("$.description", is(taskResponse.getDescription())));

        verify(taskService).create(any(TaskRequest.class));
    }

    @Test
    @DisplayName("Should update task and return updated task response")
    void testUpdateTask() throws Exception {
        when(taskService.update(any(UUID.class), any(TaskRequest.class))).thenReturn(taskResponse);

        mockMvc.perform(put("/tasks/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "Updated Task",
                                  "description": "Updated Task Description",
                                  "projectId": "c1234567-d89b-42c3-a456-556642440000"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(taskResponse.getId().toString())))
                .andExpect(jsonPath("$.title", is(taskResponse.getTitle())))
                .andExpect(jsonPath("$.description", is(taskResponse.getDescription())));

        verify(taskService).update(any(UUID.class), any(TaskRequest.class));
    }

    @Test
    @DisplayName("Should update task with customer and return updated task response")
    void testUpdateTaskWithCustomer() throws Exception {
        when(taskService.update(any(UUID.class), any(UUID.class))).thenReturn(taskResponse);

        mockMvc.perform(put("/tasks/{id}/customers/{customerId}", taskId, customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(taskResponse.getId().toString())))
                .andExpect(jsonPath("$.title", is(taskResponse.getTitle())))
                .andExpect(jsonPath("$.description", is(taskResponse.getDescription())));

        verify(taskService).update(any(UUID.class), any(UUID.class));
    }

    @Test
    @DisplayName("Should find task by ID and return task response")
    void testFindTaskById() throws Exception {
        when(taskService.findById(any(UUID.class))).thenReturn(taskResponse);

        mockMvc.perform(get("/tasks/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(taskResponse.getId().toString())))
                .andExpect(jsonPath("$.title", is(taskResponse.getTitle())))
                .andExpect(jsonPath("$.description", is(taskResponse.getDescription())));

        verify(taskService).findById(any(UUID.class));
    }

    @Test
    @DisplayName("Should delete task and return no content")
    void testDeleteTask() throws Exception {
        doNothing().when(taskService).delete(any(UUID.class));

        mockMvc.perform(delete("/tasks/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(taskService).delete(any(UUID.class));
    }
}
