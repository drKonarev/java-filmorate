package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@RestController()
@RequestMapping("/genres")
@Slf4j
public class GenreController {

    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping(value ="/{id}")
    public Genre getGenre (@PathVariable("id") Integer genre_id){
        return genreService.getGenre(genre_id);
    }

    @GetMapping
    public List<Genre> getGenre (){
        return genreService.getAllGenres();
    }
}
