package ru.yandex.practicum.filmorate.storage.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.ArrayList;
import java.util.List;

import static org.zalando.logbook.BodyReplacers.stream;

@Component
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    private final Logger log = LoggerFactory.getLogger(MpaDbStorage.class);

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa getMpa(Integer id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from MPA where MPA_ID = ?", id);
        if (userRows.next()) {
            Mpa mpa = build(userRows);
            log.info("Найден рейтинг: " + userRows.getString("NAME"));
            return mpa;
        }
        throw new MpaNotFoundException("Рейтинг не найден");
    }

    @Override
    public List<Mpa> getAllMpa() {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from MPA ORDER BY MPA_ID ASC");
        List<Mpa> allMpa = new ArrayList<>();

        while (userRows.next()) {
            allMpa.add(build(userRows));
        }
        if (allMpa.isEmpty()) log.info("Список рейтингов пуст!");
        else {
            log.info("Всего типов рейтинга: " + allMpa.size());
        }

        return allMpa;
    }

    private Mpa build(SqlRowSet userRows) {
        if (userRows.wasNull()) throw new MpaNotFoundException("Рейтинг не найден");
        return Mpa.builder()
                .id(userRows.getInt("MPA_ID"))
                .name(userRows.getString("NAME"))
                .build();
    }
}
