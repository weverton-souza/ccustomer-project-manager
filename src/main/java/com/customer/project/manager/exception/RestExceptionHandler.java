package com.customer.project.manager.exception;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public @ResponseBody ResponseEntity<Object> handleResourceNotFoundException(final ResourceNotFoundException ex) {
        ExceptionDetails exceptionDetails = new ExceptionDetails(
                this.timestampConverter(new Date().getTime()),
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND,
                HttpStatus.NOT_FOUND.getReasonPhrase()
        );

        return new ResponseEntity<>(exceptionDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMessageNotWritableException.class)
    public ResponseEntity<ExceptionDetails> handleSerializationError(HttpMessageNotWritableException ex) {
        ExceptionDetails exceptionDetails = new ExceptionDetails(
                this.timestampConverter(new Date().getTime()),
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exceptionDetails);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected @ResponseBody ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex) {
        HashMap<String, List<String>> fieldExceptionDetails = new HashMap<>();
        List<FieldExceptionDetails> detailsList = new ArrayList<>();
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        for (FieldError e : fieldErrors) {
            if (fieldExceptionDetails.get(e.getField()) == null) {
                fieldExceptionDetails.put(e.getField(), new ArrayList<>());
                fieldExceptionDetails.get(e.getField()).add(e.getDefaultMessage());
            } else {
                fieldExceptionDetails.get(e.getField()).add(e.getDefaultMessage());
            }
        }

        for (Map.Entry<String, List<String>> entry : fieldExceptionDetails.entrySet()) {
            String k = entry.getKey();
            List<String> v = entry.getValue();
            detailsList.add(new FieldExceptionDetails(k, v));
        }

        ValidationExceptionDetails validationExceptionDetails =
                ValidationExceptionDetails.Builder()
                        .withFieldExceptionDetails(detailsList)
                        .withTimestamp(this.timestampConverter(new Date().getTime()))
                        .withDeveloperMessage(ex.getClass().getName())
                        .build();

        return new ResponseEntity<>(validationExceptionDetails, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody ResponseEntity<Object> handleAllUncaughtException(
            Exception ex, WebRequest request) {
        ExceptionDetails exceptionDetails = new ExceptionDetails(
                this.timestampConverter(new Date().getTime()),
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()
        );

        return new ResponseEntity<>(exceptionDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String timestampConverter (final long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        return formatter.format(date);
    }
}
