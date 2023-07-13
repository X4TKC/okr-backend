package com.upb.okrbackend;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
@ControllerAdvice
public class OkrExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {OkrExceptionErrors.class})
    public ResponseEntity<Object> handleUserAlreadyExceptions(Exception exception, WebRequest webRequest) {
        HttpStatus errorCode = HttpStatus.INTERNAL_SERVER_ERROR;

        return this.handleExceptionInternal(exception, new ErrorInfo(errorCode.value(), "user already created"), new HttpHeaders(), errorCode, webRequest);
    }
}
