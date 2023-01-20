package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
   private final FilmStorage filmStorage;
   private final UserStorage userStorage;

    private final Logger log = LoggerFactory.getLogger(FilmService.class);

    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage, @Qualifier("UserDbStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film get(Integer id) {
        return filmStorage.get(id);
    }

    public Film post(Film film) {
        return filmStorage.post(film);
    }

    public Film put(Film film) {
        return filmStorage.put(film);
    }

    public void delete(Integer id) {
        filmStorage.delete(id);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public List<Film> getTopFilms(int count) {
        return filmStorage.getTopFilms(count);


    }


    public void like(Integer filmId, Integer userId) {
        filmStorage.like(filmId, userId);
    }

    public void dislike(Integer filmId, Integer userId) {
        filmStorage.disLike(filmId, userId);
    }


}
