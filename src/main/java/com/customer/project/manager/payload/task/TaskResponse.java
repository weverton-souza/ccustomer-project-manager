package com.customer.project.manager.payload.task;

import com.customer.project.manager.enumeration.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.UUID;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "Response para Task")
public class TaskResponse {

    @Schema(description = "ID da tarefa", example = "t1234567-d89b-42c3-a456-556642440000")
    private UUID id;

    @Schema(description = "Título da tarefa", example = "Implement login")
    private String title;

    @Schema(description = "Descrição da tarefa", example = "Develop the login functionality")
    private String description;

    @Schema(description = "Status da tarefa", example = "To Do", enumAsRef = true, implementation = TaskStatus.class)
    private TaskStatus status;

    @Schema(description = "ID do projeto associado", example = "p1234567-d89b-42c3-a456-556642440000")
    private UUID projectId;
}
