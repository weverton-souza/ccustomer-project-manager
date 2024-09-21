package com.customer.project.manager.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionDetails {
    private String timestamp;
    private String developerMessage;
    private Integer code;
    private HttpStatus status;
    private String description;
}
