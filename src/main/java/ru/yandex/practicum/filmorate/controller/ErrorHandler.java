package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({UserNotFoundException.class, FilmNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notFound(RuntimeException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler({UserAlreadyExistException.class, FilmAlreadyExistException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse alreadyExist(RuntimeException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler({ValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse validationError(IllegalArgumentException ex) {
        return new ErrorResponse(ex.getMessage());
    }


    @ExceptionHandler ({Throwable.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse internalError(Throwable ex) {
        return new ErrorResponse("Happened something internal!");
    }


}