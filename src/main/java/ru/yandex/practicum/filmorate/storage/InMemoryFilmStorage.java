package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private static final LocalDate FIRST_DATE = LocalDate.of(1895, 12, 28);
    private final HashMap<Integer, Film> films = new HashMap<>();

    private int id = 0;

    @Override
    public void delete(Integer id) {
        doesExist(id);
        films.remove(id);
    }

    @Override
    public Film put(Film film) {
        validate(film);
        doesExist(film.getId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film post(Film film) {
        validate(film);
        film.setId(++id);
        if (films.containsKey(film.getId()))
            throw new FilmAlreadyExistException("Film already exist!");
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film get(Integer id) {
        doesExist(id);
        return films.get(id);
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    private void validate(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(FIRST_DATE)) {
            throw new ValidationException("Release time is too early for movie!");
        }
        if (film.getName().isBlank()) {
            throw new ValidationException("Incorrect film data!");
        }
    }

    @Override
    public void like(Integer id, Integer userId) {
        doesExist(id);
        films.get(id).getLikes().add(userId);
    }

    @Override
    public void disLike(Integer id, Integer userId) {
        doesExist(id);
        if (!films.get(id).getLikes().contains(userId))
            throw new UserNotFoundException("This user don't like film early!");
        films.get(id).getLikes().remove(userId);
    }


    private void doesExist(Integer id) {
        if (!films.containsKey(id))
            throw new FilmNotFoundException("Not found film with such id - " + id);
    }
}
