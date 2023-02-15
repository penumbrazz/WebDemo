package com.example.demo.common.exception;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public void handleValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        String message = null;
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                message = fieldError.getField()+fieldError.getDefaultMessage();
            }
        }
    }

    @ResponseBody
    @ExceptionHandler(value= RuntimeException.class)
    public void handleRuntimeException(RuntimeException e){
        //todo:log
    }
    @ResponseBody
    @ExceptionHandler(value = IOException.class)
    public void handleIOException(IOException e){

    }
}
