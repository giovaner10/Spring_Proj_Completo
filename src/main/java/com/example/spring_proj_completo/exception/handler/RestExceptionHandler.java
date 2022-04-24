package com.example.spring_proj_completo.exception.handler;

import com.example.spring_proj_completo.exception.BadRequestException;
import com.example.spring_proj_completo.exception.BadRequestExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BadRequestExceptionDetails> handlerBadRequestException(BadRequestException e){
        BadRequestExceptionDetails exceptionDetails = createException(HttpStatus.NOT_FOUND,
                ExceptionType.ENITDADE_NAO_ENCONTRADA,
                e.getLocalizedMessage()).developerMessage(e.getClass().getName()).build();

        return new ResponseEntity<>(exceptionDetails, HttpStatus.NOT_FOUND);

    }


    public BadRequestExceptionDetails.BadRequestExceptionDetailsBuilder createException(HttpStatus status, ExceptionType exceptionType, String detail){
        return BadRequestExceptionDetails.builder()
                .status(status.value())
                .details(exceptionType.getUri())
                .title(exceptionType.getTitle())
                .timestamp(LocalDateTime.now());

    }
}
