package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {

    public Genre getGenre (Integer id);

    public List<Genre> getAllGenres();
}
