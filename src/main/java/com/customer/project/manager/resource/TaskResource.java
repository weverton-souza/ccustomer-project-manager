package com.customer.project.manager.resource;

import com.customer.project.manager.payload.task.TaskRequest;
import com.customer.project.manager.payload.task.TaskResponse;
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

@Tag(name = "Task Resources", description = "Resources for managing tasks")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public interface TaskResource {

    @Operation(summary = "Create a new task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    ResponseEntity<TaskResponse> create(
            @Valid @RequestBody TaskRequest request);

    @Operation(summary = "Update an existing task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task updated successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    ResponseEntity<TaskResponse> update(
            @Parameter(description = "ID of the task to be updated") @PathVariable UUID id,
            @Valid @RequestBody TaskRequest request);

    @Operation(summary = "Find a task by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task found successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    ResponseEntity<TaskResponse> findById(
            @Parameter(description = "ID of the task to be retrieved") @PathVariable UUID id);

    @Operation(summary = "Find all tasks")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully")
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
    ResponseEntity<Page<TaskResponse>> findAll(
            @RequestParam(value = "page", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = "15") int pageSize
    );

    @Operation(summary = "Delete a task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    ResponseEntity<Void> delete(
            @Parameter(description = "ID of the task to be deleted") @PathVariable UUID id);
}
