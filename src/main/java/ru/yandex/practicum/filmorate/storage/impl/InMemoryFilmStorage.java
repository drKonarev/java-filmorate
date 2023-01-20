package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;


import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Qualifier("memory")
public class InMemoryFilmStorage implements FilmStorage {
    private static final LocalDate FIRST_DATE = LocalDate.of(1895, 12, 28);
    private final HashMap<Integer, Film> films = new HashMap<>();

    private Map <Integer, List <Integer>> likes = new HashMap<>();

    private int id = 0;
    public static <K, V extends Comparable<V>> Map<K, V> sortByValues(final Map<K, V> map) {
        Comparator<K> valueComparator = (k1, k2) -> {
            int compare = map.get(k2).compareTo(map.get(k1));
            if (compare == 0) return 1;
            else return compare;
        };
        Map<K, V> sortedByValues = new TreeMap<K, V>(valueComparator);
        sortedByValues.putAll(map);
        return sortedByValues;
    }
    @Override
    public List<Film> getTopFilms(Integer count){
        Map <Integer, Integer> likesCount = new HashMap<>();
        for (Integer id:likes.keySet()){
            likesCount.put(id, likes.get(id).size());
        }
        List <Film> top = new ArrayList<>();
        return top;

    }
    @Override
    public void delete(Integer id) {
        check(id);
        films.remove(id);
    }

    @Override
    public Film put(Film film) {
        validate(film);
        check(film.getId());
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
        check(id);
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
        check(id);
        likes.get(id).add(userId);

    }

    @Override
    public void disLike(Integer id, Integer userId) {
        check(id);
        likes.get(id).remove(userId);
    }


    @Override
    public void check(Integer id) {
        if (!films.containsKey(id))
            throw new FilmNotFoundException("Not found film with such id - " + id);
    }


}
