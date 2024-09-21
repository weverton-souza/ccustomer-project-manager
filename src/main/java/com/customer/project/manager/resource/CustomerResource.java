package com.customer.project.manager.resource;

import com.customer.project.manager.payload.customer.CustomerRequest;
import com.customer.project.manager.payload.customer.CustomerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Customer Resources", description = "Resources for managing customers")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public interface CustomerResource extends BaseResource {

    @Operation(summary = "Create a new customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    ResponseEntity<CustomerResponse> create(
            @RequestBody
            CustomerRequest request
    );

    @Operation(summary = "Update an existing customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer updated successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    ResponseEntity<CustomerResponse> update(
            @Parameter(description = "ID of the customer to be updated") @PathVariable UUID id,
            @RequestBody CustomerRequest request
    );

    @Operation(summary = "Find a customer by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer found successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    ResponseEntity<CustomerResponse> findById(
            @Parameter(description = "ID of the customer to be retrieved") @PathVariable UUID id);

    @Operation(summary = "Find all customers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customers retrieved successfully")
    })
    @Parameters({
            @Parameter(
                    name = "page",
                    in = ParameterIn.QUERY,
                    description = "Número da página para a paginação dos resultados",
                    example = "0",
                    required = false
            ),
            @Parameter(
                    name = "pageSize",
                    in = ParameterIn.QUERY,
                    description = "Número de elementos por página",
                    example = "15",
                    required = false
            ),
            @Parameter(
                    name = "parameters",
                    in = ParameterIn.QUERY,
                    description = "Parâmetros adicionais para a busca",
                    required = false,
                    hidden = true
            )
    })
    ResponseEntity<Page<CustomerResponse>> findAll(
            @RequestParam Map<String, String> parameters
    );

    @Operation(summary = "Delete a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Customer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    ResponseEntity<Void> delete(@Parameter(description = "ID of the customer to be deleted") @PathVariable UUID id);
}
