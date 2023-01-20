package ru.yandex.practicum.filmorate.storage.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

import java.util.*;


@Component
@Qualifier("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private static final LocalDate FIRST_DATE = LocalDate.of(1895, 12, 28);
    private final JdbcTemplate jdbcTemplate;

    private final UserStorage userStorage;

    private final GenreStorage genreStorage;

    private final MpaStorage mpaStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, @Qualifier("UserDbStorage") UserStorage userStorage,
                         GenreStorage genreStorage, MpaStorage mpaStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userStorage = userStorage;
        this.genreStorage = genreStorage;
        this.mpaStorage = mpaStorage;
    }

    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);

    @Override
    public void delete(Integer id) {
        check(id);

        jdbcTemplate.update("delete   from FILMS where FILM_ID = ? ", id);
        log.info("Удалён фильм с id : {} ", id);
    }

    @Override
    public Film put(Film film) {
        check(film.getId());

        String sql = ("UPDATE FILMS  SET NAME=?, DESCRIPTION=?, DURATION=?, MPA_ID=?, RELEASE_DATE=? WHERE FILM_ID=" + film.getId());
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getReleaseDate());

        jdbcTemplate.update("delete from FILM_GENRE where FILM_ID = ? ", film.getId()); //обнуление информации о жанрах фильма

        genreStorage.postGenresByFilm(film);

        film.getGenres().clear();

        film.getGenres().addAll(genreStorage.getGenresByFilm(film.getId()));

        log.info("Обновлен фильм с id: {} !", film.getId());
        return film;
    }

    @Override
    public Film post(Film film) {
        if (film.getReleaseDate().isBefore(FIRST_DATE))
            throw new ValidationException("Фильмов тогда еще не было! Проверьте дату релиза!");

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILMS")
                .usingGeneratedKeyColumns("FILM_ID");
        Map<String, Object> parameters = new HashMap<>();

        parameters.put("NAME", film.getName());
        parameters.put("DESCRIPTION", film.getDescription());
        parameters.put("DURATION", film.getDuration());
        parameters.put("RELEASE_DATE", film.getReleaseDate());
        parameters.put("MPA_ID", film.getMpa().getId());

        film.setId(simpleJdbcInsert.executeAndReturnKey(parameters).intValue());

        genreStorage.postGenresByFilm(film);

        log.info("Добавлен фильм под названием {} с id: {}", film.getName(), film.getId());


        return film;
    }

    @Override
    public Film get(Integer id) {
        check(id);
        Film film = null;
        String sql = ("SELECT * FROM FILMS WHERE FILM_ID=?");
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, id);
        if (sqlRowSet.next()) film = filmFromRows(sqlRowSet).get();

        log.info("Найден фильм с id = {}", id);
        return film;

    }

    @Override
    public List<Film> getAllFilms() {

        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from FILMS");

        List<Film> films = new ArrayList<>();
        while (filmRows.next()) {
            films.add(filmFromRows(filmRows).get());
        }
        if (films.isEmpty()) log.info("Список фильмов пуст!");
        else {
            log.info("Количество всех фильмов = {}!", films.size());
        }
        return films;
    }

    @Override
    public List<Film> getTopFilms(Integer count) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT f.*, COUNT(l.USER_ID) AS likes " + //SELECT *
                "FROM (FILMS AS f INNER JOIN LIKES AS l ON f.FILM_ID =l.FILM_ID ) " +
                "GROUP BY f.FILM_ID " +
                "ORDER BY likes DESC ");

        List<Film> films = new ArrayList<>();

        while (filmRows.next()) {
            films.add(filmFromRows(filmRows).get());
        }
        if (films.isEmpty())
            films.addAll(getAllFilms());

        log.info("Количество всех фильмов в рейтинге : {}!", films.size());

        if (films.size() <= count) {
            return films;
        } else {
            return films.subList(0, count);
        }

    }

    @Override
    public void like(Integer filmId, Integer userId) {
        check(filmId);
        userStorage.check(userId);

        String sql = ("INSERT INTO LIKES (USER_ID, FILM_ID) VALUES (?, ?)");
        jdbcTemplate.update(sql, userId, filmId);
        log.info("Пользователю (id ={}) понравился фильм (filmId={})", userId, filmId);
    }

    @Override
    public void disLike(Integer filmId, Integer userId) {
        check(filmId);
        userStorage.check(userId);

        String sql = ("DELETE FROM LIKES  WHERE (USER_ID= ? AND FILM_ID=?)");
        jdbcTemplate.update(sql, userId, filmId);
        log.info("Пользователю (id ={})  перестал нравиться фильм (filmId={})", userId, filmId);
    }

    private Optional<Film> filmFromRows(SqlRowSet userRows) {

        Film film = Film.builder()
                .name(userRows.getString("NAME"))
                .mpa(mpaStorage.getMpa(userRows.getInt("MPA_ID")))
                .releaseDate(userRows.getDate("RELEASE_DATE").toLocalDate())
                .duration(userRows.getInt("DURATION"))
                .description(userRows.getString("DESCRIPTION"))
                .id(userRows.getInt("FILM_ID"))
                .build();


        film.getGenres().addAll(genreStorage.getGenresByFilm(film.getId()));

        log.info("Найден фильм с id: {} с названием: {}", film.getId(), film.getName());

        return Optional.of(film);
    }

    @Override
    public void check(Integer id) {
        String sqlCheck = ("SELECT * FROM FILMS WHERE FILM_ID=?");

        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlCheck, id);
        if (!sqlRowSet.next()) throw new FilmNotFoundException("Не найден фильм с id = " + id);
    }
}
