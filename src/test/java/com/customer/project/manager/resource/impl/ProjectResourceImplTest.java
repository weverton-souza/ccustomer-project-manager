package com.customer.project.manager.resource.impl;

import com.customer.project.manager.enumeration.ProjectStatus;
import com.customer.project.manager.payload.project.ProjectRequest;
import com.customer.project.manager.payload.project.ProjectResponse;
import com.customer.project.manager.service.ProjectService;
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

@DisplayName("ProjectResourceImpl Tests")
class ProjectResourceImplTest {

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private ProjectResourceImpl projectResource;

    private MockMvc mockMvc;

    private ProjectResponse projectResponse;
    private UUID projectId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(projectResource).build();

        projectId = UUID.randomUUID();

        projectResponse = new ProjectResponse();
        projectResponse.setId(projectId);
        projectResponse.setName("New Project");
        projectResponse.setDescription("Project Description");
        projectResponse.setStatus(ProjectStatus.valueOf("OPEN"));
    }

    @Test
    @DisplayName("Should create project and return created project response")
    void testCreateProject() throws Exception {
        when(projectService.create(any(ProjectRequest.class))).thenReturn(projectResponse);

        mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "name": "New Project",
                              "description": "Project Description",
                              "customerId": "c1234567-d89b-42c3-a456-556642440000",
                              "status": "OPEN"
                            }
                            """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(projectResponse.getId().toString())))
                .andExpect(jsonPath("$.name", is(projectResponse.getName())))
                .andExpect(jsonPath("$.description", is(projectResponse.getDescription())))
                .andExpect(jsonPath("$.status", is("OPEN")));

        verify(projectService).create(any(ProjectRequest.class));
    }

    @Test
    @DisplayName("Should update project and return updated project response")
    void testUpdateProject() throws Exception {
        when(projectService.update(any(UUID.class), any(ProjectRequest.class))).thenReturn(projectResponse);

        mockMvc.perform(put("/projects/{id}", projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Updated Project",
                                  "description": "Updated Description",
                                  "customerId": "c1234567-d89b-42c3-a456-556642440000",
                                  "status": "IN_PROGRESS"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(projectResponse.getId().toString())))
                .andExpect(jsonPath("$.name", is(projectResponse.getName())))
                .andExpect(jsonPath("$.description", is(projectResponse.getDescription())))
                .andExpect(jsonPath("$.status", is("OPEN")));

        verify(projectService).update(any(UUID.class), any(ProjectRequest.class));
    }

    @Test
    @DisplayName("Should find project by ID and return project response")
    void testFindProjectById() throws Exception {
        when(projectService.findById(any(UUID.class))).thenReturn(projectResponse);

        mockMvc.perform(get("/projects/{id}", projectId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(projectResponse.getId().toString())))
                .andExpect(jsonPath("$.name", is(projectResponse.getName())))
                .andExpect(jsonPath("$.description", is(projectResponse.getDescription())))
                .andExpect(jsonPath("$.status", is("OPEN")));

        verify(projectService).findById(any(UUID.class));
    }

    @Test
    @DisplayName("Should delete project and return no content")
    void testDeleteProject() throws Exception {
        doNothing().when(projectService).delete(any(UUID.class));

        mockMvc.perform(delete("/projects/{id}", projectId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(projectService).delete(any(UUID.class));
    }
}
