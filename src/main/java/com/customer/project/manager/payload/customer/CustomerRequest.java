package com.customer.project.manager.payload.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString
@Accessors(chain = true)
@Schema(description = "Request para criação/atualização de Customer")
public class CustomerRequest {

    @NotBlank
    @Size(max = 255)
    @Schema(description = "Nome do cliente", example = "John Doe")
    private String name;

    @NotBlank
    @Size(max = 255)
    @Schema(description = "Senha do cliente", example = "123456")
    private String password;

    @Email
    @NotBlank
    @Size(max = 255)
    @Schema(description = "Email do cliente", example = "john.doe@example.com")
    private String email;

    @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Número de telefone inválido")
    @Schema(description = "Telefone do cliente", example = "+1234567890")
    private String phone;
}

