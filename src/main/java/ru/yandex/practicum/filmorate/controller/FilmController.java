package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController()
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private static final LocalDate FIRST_DATE = LocalDate.of(1895, 12, 28);
    private final HashMap<Integer, Film> films = new HashMap<>();
    private int id = 0;

    @PostMapping()
    public Film post(@Valid @RequestBody Film film) {
        validate(film);
        film.setId(++id);
        films.put(id, film);
        return film;
    }

    @GetMapping()
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @PutMapping()
    public Film update(@Valid @RequestBody Film film) {
        validate(film);
        if (!(films.containsKey(film.getId()))) {
            throw new ValidationException("Film with this id doesn't exist!");
        }
        films.put(film.getId(), film);
        return film;
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") int filmId) {
        films.remove(filmId);
    }

    private void validate(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(FIRST_DATE)) {
            throw new ValidationException("Release time is too early for movie!");
        }
        if (film.getName().isBlank()) {
            throw new ValidationException("Incorrect film data!");
        }
    }

    public void test(Film film) {
        validate(film);
    }

}
