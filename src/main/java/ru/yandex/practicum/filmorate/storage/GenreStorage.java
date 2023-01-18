package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {

     Genre getGenre (Integer id);

     List<Genre> getAllGenres();

     void postGenresByFilm(Film film);

     List<Genre> getGenresByFilm(Integer filmId);
}
