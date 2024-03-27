package com.cms.audit.api.Common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class ItemAlreadyExistException extends RuntimeException{
    public ItemAlreadyExistException(String message) {
        super(message);
    }
}
