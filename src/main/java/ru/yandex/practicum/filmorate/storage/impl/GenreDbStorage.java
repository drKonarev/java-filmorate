package ru.yandex.practicum.filmorate.storage.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final Logger log = LoggerFactory.getLogger(GenreStorage.class);

    @Override
    public Genre getGenre(Integer id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from GENRES where GENRE_ID = ?", id);
        if (userRows.next()) {
            Genre genre = build(userRows);
            log.info("Найден жанр: " + userRows.getString("NAME"));
            return genre;
        }
        throw new MpaNotFoundException("Рейтинг не найден");
    }

    @Override
    public List<Genre> getAllGenres() {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from GENRES ORDER BY GENRE_ID ASC");
        List<Genre> allGenres = new ArrayList<>();
        while (userRows.next()) {
            allGenres.add(build(userRows));
        }
        log.info("Всего жанров: " + allGenres.size());
        return allGenres;
    }

    @Override
    public List<Genre> getGenresByFilm(Integer filmId) {
        List<Genre> genres = new ArrayList<>();
        String genresSql = "SELECT * FROM FILM_GENRE WHERE FILM_ID =? ORDER BY GENRE_ID ASC";
        SqlRowSet sqlGenres = jdbcTemplate.queryForRowSet(genresSql, filmId);
        while (sqlGenres.next()) {
            genres.add(
                    getGenre(sqlGenres.getInt("GENRE_ID")));
        }

        return genres;
    }


    @Override
    public void postGenresByFilm(Film film) {

        if (!film.getGenres().isEmpty()) {
            try {
                jdbcTemplate.getDataSource().getConnection().setAutoCommit(false);
                try (PreparedStatement ps = jdbcTemplate.getDataSource().getConnection().prepareStatement
                        ("INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (?, ?)")) {
                    for (Genre genre : film.getGenres()) {
                        ps.setInt(1, film.getId());
                        ps.setInt(2, genre.getId());

                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Genre build(SqlRowSet userRows) {
        if (userRows.wasNull()) throw new GenreNotFoundException("Жанр  не найден");
        return Genre.builder()
                .id(userRows.getInt("GENRE_ID"))
                .name(userRows.getString("NAME"))
                .build();


    }
}
