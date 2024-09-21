package com.customer.project.manager.payload.security;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Resposta de SignIn", description = "Response de resposta contendo o token de acesso JWT, tempo de expiração e tipo de token após a autenticação bem-sucedida")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignInResponse {

    @Schema(description = "Token JWT para a sessão autenticada", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String accessToken;

    @Schema(description = "O tempo de expiração do token JWT em milissegundos", example = "3600000")
    private Long expiration;

    @Schema(description = "Tipo do token, por exemplo, Bearer", example = "Bearer")
    private String tokenType;
}
