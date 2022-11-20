package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    FilmStorage filmStorage;
    UserStorage userStorage;

    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film get(Integer id) {
        return filmStorage.get(id);
    }

    public Film post(Film film) {
        film.setLikes(new HashSet<>());
        return filmStorage.post(film);
    }

    public Film put(Film film) {
        film.setLikes(new HashSet<>());
        return filmStorage.put(film);
    }

    public void delete(Integer id) {
        filmStorage.delete(id);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public List<Film> getTopFilms(int count) {
        return filmStorage.getAllFilms().stream()
                .sorted((o1, o2) -> o2.getLikes().size() - o1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());

    }

    public void like(Integer filmId, Integer userId) {
        userStorage.doesExist(userId); // проверяем существование пользователя
        filmStorage.like(filmId, userId);
    }

    public void dislike(Integer filmId, Integer userId) {
        userStorage.doesExist(userId);
        filmStorage.disLike(filmId, userId);
    }


}
