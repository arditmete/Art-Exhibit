package com.edu.artexhibit.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class RestControllerAdvisor{

    @ExceptionHandler({ArtExhibitException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleCustomException(ArtExhibitException e) {
        log.error("Runtime Exception thrown: {0}", e);
        return new ErrorResponseDto(e.getCode().getCode(), e.getDetails(), LocalDateTime.now());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleRequestBodyNotValidException(MethodArgumentNotValidException exception) {
        log.warn(exception.getMessage());
        return new ErrorResponseDto(ErrorCode.BAD_REQUEST.getCode(), mapErrorExceptions(exception), LocalDateTime.now());
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        log.warn(exception.getMessage());
        return new ErrorResponseDto(ErrorCode.BAD_REQUEST.getCode(), exception.getMessage(), LocalDateTime.now());
    }

    private List<String> mapErrorExceptions(MethodArgumentNotValidException exception){
        List<String> bindingResultErrors = new ArrayList<>();
        exception.getBindingResult().getAllErrors().forEach(error-> bindingResultErrors.add(error.getDefaultMessage()));
        return bindingResultErrors;
    }
}