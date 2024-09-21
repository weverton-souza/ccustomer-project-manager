package com.customer.project.manager.resource;

import com.customer.project.manager.exception.ExceptionDetails;
import com.customer.project.manager.payload.security.SignInRequest;
import com.customer.project.manager.payload.security.SignInResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Tag(name = "Security Manager Resources", description = "Recursos para gerenciar a segurança da aplicação")
@RequestMapping("/security")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public interface SecurityManagerResource {
    @Operation(summary = "Serviço para autenticação de usuário", description = "Este endpoint permite a autenticação de um usuário fornecendo as credenciais no corpo da requisição.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário autenticado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SignInResponse.class))),
            @ApiResponse(responseCode = "400", description = "Erro de validação",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDetails.class))),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDetails.class)))
    })
    ResponseEntity<SignInResponse> signIn(
            @Parameter(description = "Credenciais para autenticação do usuário")
            @Valid @RequestBody SignInRequest request);

}
