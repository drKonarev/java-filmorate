package ru.yandex.practicum.filmorate.storage.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;


import java.util.*;


@Component
@Qualifier("UserDbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final Logger log = LoggerFactory.getLogger(UserDbStorage.class);

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void delete(Integer id) {

        if (jdbcTemplate.update(" delete  from USERS where USER_ID = ?", id) > 0)
            log.info("Удалён пользователь с id : {} ", id);
        else {
            throw new UserNotFoundException("Не найден пользователь с id = " + id);
        }
    }

    @Override
    public User post(User user) {

        validate(user);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("USERS")
                .usingGeneratedKeyColumns("USER_ID");

        Map<String, Object> parameters = new HashMap<>();

        if (user.getName().isBlank() || user.getName().isEmpty()) {
            parameters.put("NAME", user.getLogin());
        } else {
            parameters.put("NAME", user.getName());
        }
        parameters.put("LOGIN", user.getLogin());
        parameters.put("EMAIL", user.getEmail());
        parameters.put("BIRTHDAY", user.getBirthday());

        user.setId(simpleJdbcInsert.executeAndReturnKey(parameters).intValue());

        log.info("Добавлен пользователь с id: {} !", user.getId());
        return user;
    }

    @Override
    public User put(User user) {
        validate(user);
        check(user.getId());

        String sqlQuery = ("UPDATE USERS SET NAME=?, EMAIL = ?, BIRTHDAY = ?, LOGIN = ? WHERE USER_ID=" + user.getId());
        jdbcTemplate.update(sqlQuery, user.getName(),
                user.getEmail(),
                user.getBirthday(),
                user.getLogin());

        log.info("Обновлен пользователь с id: {} !", user.getId());

        return user;
    }

    @Override
    public Optional<User> get(Integer id) {
        check(id);
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from USERS where USER_ID = ?", id);
        if (userRows.wasNull() || !userRows.next()) check(id);
        log.info("Найден пользователь с идентификатором {}.", id);
        return userFromSqlRow(userRows);
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from USERS");
        if (userRows.wasNull()) {
            log.info("Список пользователей пуст!");
            throw new UserNotFoundException("Не найдено ни одного пользователя!");
        }
        while (userRows.next()) {
            users.add(userFromSqlRow(userRows).get());
        }

        return users;
    }

    @Override
    public List<User> getAllFriends(Integer id) {
        check(id);

        List<User> friends = new ArrayList<>();

        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM USERS u WHERE \n" +
                "USER_ID IN  (SELECT FRIEND_ID from USERS INNER JOIN FRIENDS ON   users.user_id=FRIENDS.USER_id" +
                " WHERE USERS.USER_ID=?);", id); // запрос на выгрузку всех друзей по айди юзера

        while (userRows.next()) {
            friends.add(userFromSqlRow(userRows).get());
        }
        if (friends.isEmpty()) log.info("Список пользователей пуст!");
        else {
            log.info("Найдены друзья пользователя c id:" + id);
        }

        return friends;
    }

    public List<User> getCommonFriends(Integer id, Integer friendId) {
        check(id);
        check(friendId);

        List<User> friends = new ArrayList<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(" SELECT * FROM USERS  WHERE USER_ID IN" +
                " ( SELECT FRIEND_ID FROM FRIENDS f WHERE USER_ID=? AND FRIEND_ID  IN " +
                "(  SELECT FRIEND_ID  FROM FRIENDS f WHERE USER_ID=?))", id, friendId);

        while (userRows.next()) {
            friends.add(userFromSqlRow(userRows).get());
        }
        if (friends.isEmpty()) log.info("Общих друзей нет!");
        else {
            log.info("Найдены общие друзья между пользователями : " + id + " и " + friendId);
        }

        return friends;
    }

    @Override
    public void addFriend(Integer userId, Integer friendId) {
        check(userId);
        check(friendId);

        jdbcTemplate.update(" INSERT INTO FRIENDS VALUES (?, ?)",  friendId,userId);
        log.info("Пользователь id: {} отправил запрос на дружбу пользователю id: {} ",  userId, friendId);
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
        check(userId);
        check(friendId);

        if (jdbcTemplate.update(" delete  from FRIENDS where USER_ID = ? AND FRIEND_ID=?", userId, friendId) > 0)
            log.info("Дружбу с пользователем: {} прервал пользователь: {} ", friendId, userId);
    }

    private Optional<User> userFromSqlRow(SqlRowSet userRows) {

        User user = User.builder()
                .name(userRows.getString("NAME"))
                .email(userRows.getString("EMAIL"))
                .login(userRows.getString("LOGIN"))
                .id(userRows.getInt("USER_ID"))
                .birthday((userRows.getDate("BIRTHDAY")).toLocalDate())
                .build();

        log.info("Найден пользователь с id: {} по имени: {}", user.getId(), user.getName());

        return Optional.of(user);
    }

    @Override
    public void check(Integer id) {
        String sqlCheck = ("SELECT * FROM USERS WHERE USER_ID=?");
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlCheck, id);
        if (!sqlRowSet.next()) throw new UserNotFoundException("Не найден пользователь с id = " + id);

    }

    @Override
    public void validate(User user) {
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Ошибка при валидации поля 'login'");
        }
    }
}
