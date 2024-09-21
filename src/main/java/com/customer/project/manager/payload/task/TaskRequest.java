package com.customer.project.manager.payload.task;

import com.customer.project.manager.enumeration.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString
@Accessors(chain = true)
@Schema(description = "Request para criação/atualização de Task")
public class TaskRequest {

    @NotBlank
    @Size(max = 255)
    @Schema(description = "Título da tarefa", example = "Implement login")
    private String title;

    @Size(max = 500)
    @Schema(description = "Descrição da tarefa", example = "Develop the login functionality")
    private String description;

    @NotNull
    @Schema(description = "ID do projeto associado", example = "p1234567-d89b-42c3-a456-556642440000")
    private UUID projectId;

    @NotNull
    @Schema(description = "ID do cliente associado", example = "p1234567-d89b-76g9-a456-5566424478944")
    private UUID customerId;

    @NotBlank
    @Schema(description = "Status da tarefa", example = "To Do", enumAsRef = true, implementation = TaskStatus.class)
    private TaskStatus status;
}

