package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    void delete(Integer id);

    Film put(Film film);

    Film post(Film film);

    Film get(Integer id);

    List<Film> getAllFilms();

    void like(Integer filmId, Integer userId);

    void disLike(Integer filmId, Integer userId);

}
