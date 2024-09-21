package com.customer.project.manager.payload.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "Response para Customer")
public class CustomerResponse {

    @Schema(description = "ID do cliente", example = "c1234567-d89b-42c3-a456-556642440000")
    private UUID id;

    @Schema(description = "Nome do cliente", example = "John Doe")
    private String name;

    @Schema(description = "Email do cliente", example = "john.doe@example.com")
    private String email;

    @Schema(description = "Telefone do cliente", example = "+1234567890")
    private String phone;
}
