package com.customer.project.manager.exception;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Data
@Getter
@EqualsAndHashCode(callSuper = true)
public class ValidationExceptionDetails extends ExceptionDetails {
    private List<FieldExceptionDetails> fieldExceptionDetails;

    public static ValidationExceptionDetailsBuilder Builder() {
        return new ValidationExceptionDetailsBuilder();
    }

    public static final class ValidationExceptionDetailsBuilder {
        private String timestamp;
        private String developerMessage;
        private List<FieldExceptionDetails> fieldExceptionDetails;

        private ValidationExceptionDetailsBuilder() {
        }

        public ValidationExceptionDetailsBuilder withFieldExceptionDetails(
                List<FieldExceptionDetails> fieldExceptionDetails) {
            this.fieldExceptionDetails = fieldExceptionDetails;
            return this;
        }

        ValidationExceptionDetailsBuilder withTimestamp(String timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        ValidationExceptionDetailsBuilder withDeveloperMessage(String developerMessage) {
            this.developerMessage = developerMessage;
            return this;
        }

        ValidationExceptionDetails build() {
            ValidationExceptionDetails validation = new ValidationExceptionDetails();
            validation.fieldExceptionDetails = this.fieldExceptionDetails;
            validation.setDeveloperMessage(this.developerMessage);
            validation.setTimestamp(this.timestamp);
            return validation;
        }
    }
}
