package ru.yandex.practicum.filmorate.exception;


public class FilmNotFoundException extends RuntimeException{
    public FilmNotFoundException(String mes){
        super(mes);
    }
}
