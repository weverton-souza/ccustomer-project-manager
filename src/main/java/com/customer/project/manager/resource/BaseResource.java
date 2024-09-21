package com.customer.project.manager.resource;

import com.customer.project.manager.exception.ExceptionDetails;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.Map;
import java.util.Objects;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;


import static java.util.Objects.isNull;

@ApiResponses(
        value = {
                @ApiResponse(
                        responseCode = "500",
                        description = "Erro Interno do Servidor. Um erro inesperado ocorreu no servidor.",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ExceptionDetails.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "405",
                        description = "Exceção de Validação. Método não permitido para o recurso solicitado.",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ExceptionDetails.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "400",
                        description = "ID Inválido Fornecido. O ID fornecido está em um formato inválido.",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ExceptionDetails.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "400",
                        description = "Campo(s) Inválido(s) Fornecido(s). Verifique os dados fornecidos.",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ExceptionDetails.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "Recurso Não Encontrado. O recurso solicitado não foi encontrado.",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ExceptionDetails.class)
                        )
                )
        }
)
@CrossOrigin(
        origins = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}
)
public interface BaseResource{
    default PageRequest retrievePageableParameter(Map<String, String> parameters) {
        int pageNumber = (isNull(parameters.get("pageNumber")) || parameters.get("pageNumber").isBlank())
                ? 0
                : Integer.parseInt(parameters.get("pageNumber"));

        int pageSize = (isNull(parameters.get("pageSize")) || parameters.get("pageSize").isBlank())
                ? 15
                : Integer.parseInt(parameters.get("pageSize"));

        return PageRequest.of(pageNumber, pageSize);
    }
}

