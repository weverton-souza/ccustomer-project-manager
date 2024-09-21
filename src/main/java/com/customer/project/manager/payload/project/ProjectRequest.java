package com.customer.project.manager.payload.project;

import com.customer.project.manager.enumeration.ProjectStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "Request para criação/atualização de Project")
public class ProjectRequest {

    @NotBlank
    @Size(max = 255)
    @Schema(description = "Nome do projeto", example = "New Website")
    private String name;

    @Size(max = 500)
    @Schema(description = "Descrição do projeto", example = "Website development project")
    private String description;

    @NotNull
    @Schema(description = "ID do cliente associado", example = "c1234567-d89b-42c3-a456-556642440000")
    private UUID customerId;

    @NotBlank
    @Schema(
            description = "Status do projeto",
            example = "open",
            enumAsRef = true,
            implementation = ProjectStatus.class
    )
    private ProjectStatus status;
}
