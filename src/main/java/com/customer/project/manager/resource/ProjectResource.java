package com.customer.project.manager.resource;

import com.customer.project.manager.payload.project.ProjectRequest;
import com.customer.project.manager.payload.project.ProjectResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Project Resources", description = "Resources for managing projects")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public interface ProjectResource extends BaseResource {

    @Operation(summary = "Create a new project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Project created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    ResponseEntity<ProjectResponse> create(
            @Valid @RequestBody ProjectRequest request);

    @Operation(summary = "Update an existing project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project updated successfully"),
            @ApiResponse(responseCode = "404", description = "Project not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    ResponseEntity<ProjectResponse> update(
            @Parameter(description = "ID of the project to be updated") @PathVariable UUID id,
            @Valid @RequestBody ProjectRequest request);

    @Operation(summary = "Find a project by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project found successfully"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    ResponseEntity<ProjectResponse> findById(
            @Parameter(description = "ID of the project to be retrieved") @PathVariable UUID id);

    @Operation(summary = "Find all projects")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projects retrieved successfully")
    })
    @Parameters({
            @Parameter(
                    name = "page",
                    in = ParameterIn.QUERY,
                    description = "Número da página para a paginação dos resultados",
                    example = "0",
                    required = false
            ),
            @Parameter(
                    name = "pageSize",
                    in = ParameterIn.QUERY,
                    description = "Número de elementos por página",
                    example = "15",
                    required = false
            ),
            @Parameter(
                    name = "parameters",
                    in = ParameterIn.QUERY,
                    description = "Parâmetros adicionais para a busca",
                    required = false,
                    hidden = true
            )
    })
    ResponseEntity<Page<ProjectResponse>> findAll(
            @RequestParam(value = "page", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = "15") int pageSize
    );

    @Operation(summary = "Delete a project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Project deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    ResponseEntity<Void> delete(
            @Parameter(description = "ID of the project to be deleted") @PathVariable UUID id);
}
