package com.customer.project.manager.exception;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldExceptionDetails {
    private String field;
    private List<String> fieldMessages;
}
