package com.project.urlservice.exception;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;
import org.springframework.stereotype.Component;

public class UrlAlreadyExistsException extends RuntimeException {

    public UrlAlreadyExistsException(String message) {
        super(message);
    }

}
