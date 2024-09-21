package com.customer.project.manager.payload.project;

import com.customer.project.manager.enumeration.ProjectStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "Response para Project")
public class ProjectResponse {

    @Schema(description = "ID do projeto", example = "p1234567-d89b-42c3-a456-556642440000")
    private UUID id;

    @Schema(description = "Nome do projeto", example = "New Website")
    private String name;

    @Schema(description = "Descrição do projeto", example = "Website development project")
    private String description;

    @Schema(description = "Status do projeto", example = "open", enumAsRef = true, implementation = ProjectStatus.class)
    private ProjectStatus status;

    @Schema(description = "ID do cliente associado", example = "c1234567-d89b-42c3-a456-556642440000")
    private UUID customerId;
}
