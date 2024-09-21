package com.customer.project.manager.payload.security;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "SignIn Request", description = "Request para autenticação de um usuário")
public class SignInRequest {
    @Size(max = 50)
    @NotBlank
    @Schema(description = "Email do usuário", example = "root-user@example.com")
    private String email;

    @Size(max = 50)
    @NotBlank
    @Schema(description = "Senha do usuário", example = "123")
    private String password;
}
