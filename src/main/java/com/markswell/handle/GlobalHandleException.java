package com.markswell.handle;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.context.MessageSource;
import com.markswell.exception.NotFoundException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Locale;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
@AllArgsConstructor
public class GlobalHandleException extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResponseError> handlerNotFoundException(final NotFoundException e) {
        String message = messageSource.getMessage(e.getMessage(), new Object[]{}, Locale.getDefault());
        return new ResponseEntity<>(ResponseError.builder().code(NOT_FOUND.value()).message(message).build(), NOT_FOUND);
    }


    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return badRequest();
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return badRequest();
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return badRequest();
    }

    private ResponseEntity<Object> badRequest() {
        String message = messageSource.getMessage("erro.badrequest.message", new Object[]{}, Locale.getDefault());
        return new ResponseEntity<>(ResponseError.builder().code(BAD_REQUEST.value()).message(message).build(), BAD_REQUEST);
    }
}
