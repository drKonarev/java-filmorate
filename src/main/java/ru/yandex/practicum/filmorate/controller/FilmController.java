package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

import static java.lang.String.format;

@RestController()
@RequestMapping("/films")
@Slf4j
public class FilmController {


    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping()
    public Film post(@Valid @RequestBody Film film) {
        return filmService.post(film);
    }

    @GetMapping()
    public List<Film> findAll() {
        return filmService.getAllFilms();
    }

    @GetMapping("{id}")
    public Film get(@PathVariable("id") Integer filmId) {
        return filmService.get(filmId);
    }

    @PutMapping()
    public Film put(@Valid @RequestBody Film film) {
        return filmService.put(film);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Integer filmId) {
        filmService.delete(filmId);

    }

    @PutMapping("/{id}/like/{userId}")
    public void like(@PathVariable("id") Integer id,
                     @PathVariable("userId") Integer userId) {
        filmService.like(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void dislike(@PathVariable("id") Integer id,
                        @PathVariable("userId") Integer userId) {
        filmService.dislike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> top(@RequestParam(defaultValue = "10", required = false) Integer count) {
        return filmService.getTopFilms(count);
    }


}
