package ru.yandex.practicum.filmorate;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.impl.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.impl.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@AutoConfigureTestDatabase
@Sql({"/drop_schema.sql", "/schema.sql", "/test_data.sql"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)

public class FilmorateDbTests {

    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;
    private final GenreDbStorage genreDbStorage;
    private final MpaDbStorage mpaDbStorage;

    @Test
    public void testGetUser() {

        Optional<User> userOptional = userStorage.get(1);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void testFindAllUsers() {
        List<User> users = userStorage.getAllUsers();

        assertThat(users.size()).isEqualTo(3);
    }

    @Test
    public void deleteUser() {
        userStorage.delete(1);
        assertThat(userStorage.getAllUsers().size()).isEqualTo(2);
        Assertions.assertThrows(UserNotFoundException.class, () -> userStorage.get(1));

    }

    @Test
    public void deleteWrongUser() {
        Assertions.assertThrows(UserNotFoundException.class, () -> userStorage.delete(6));
        assertThat(userStorage.getAllUsers().size()).isEqualTo(3);


    }

    @Test
    public void updateUser() {
        User updatedUser = User.builder()
                .name("UPDATE")
                .email("mail@mail.ru")
                .login("UPDATE")
                .id(1)
                .birthday(LocalDate.of(2002, 10, 10))
                .build();

        userStorage.put(updatedUser);
        assertThat(userStorage.get(1))
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                                .hasFieldOrPropertyWithValue("name", "UPDATE")
                );

    }

    @Test
    public void testFriend() {
        userStorage.addFriend(1, 3);
        assertThat(userStorage.getAllFriends(1).size()).isEqualTo(1);

        User friend = userStorage.getAllFriends(1).get(0);
        assertThat(friend).isNotNull()
                .hasFieldOrPropertyWithValue("id", 3);

        userStorage.deleteFriend(1, 3);
        assertThat(userStorage.getAllFriends(1).size()).isEqualTo(0);
    }

    @Test
    public void testCommonFriend() {
        userStorage.addFriend(1, 3);
        userStorage.addFriend(2, 3);
        assertThat(userStorage.getCommonFriends(1, 2).size()).isEqualTo(1);

        User commonFriend = userStorage.getCommonFriends(1, 2).get(0);
        assertThat(commonFriend).isNotNull()
                .hasFieldOrPropertyWithValue("id", 3);


    }

    @Test
    public void getFilmTest() {
        Film film = filmStorage.get(1);
        assertThat(film).isNotNull()
                .hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    public void getWrongFilm() {
        Assertions.assertThrows(FilmNotFoundException.class, () -> filmStorage.get(5));
    }

    @Test
    public void deleteFilm() {
        filmStorage.delete(1);
        assertThat(filmStorage.getAllFilms().size()).isEqualTo(2);
        Assertions.assertThrows(FilmNotFoundException.class, () -> filmStorage.get(1));
    }

    @Test
    public void updateFilm() {
        Film film1 = Film.builder()
                .name("UpdatedName")
                .releaseDate(LocalDate.of(2020, 10, 10))
                .duration(150)
                .description("Desc2")
                .mpa(mpaDbStorage.getMpa(2))
                .id(1)
                .build();

        film1.getGenres().clear();
        film1.getGenres().add(genreDbStorage.getGenre(2));
        film1.getGenres().add(genreDbStorage.getGenre(4));
        filmStorage.put(film1);

        assertThat(filmStorage.getAllFilms().size()).isEqualTo(3);
        assertThat(filmStorage.get(1)).isNotNull()
                .hasFieldOrPropertyWithValue("name", "UpdatedName")
                .hasFieldOrPropertyWithValue("mpa", mpaDbStorage.getMpa(2))
                .hasFieldOrPropertyWithValue("genres", Set.of(genreDbStorage.getGenre(4), genreDbStorage.getGenre(2)));

    }

    @Test
    public void getAllFilms() {
        assertThat(filmStorage.getAllFilms().size()).isEqualTo(3);
    }

    @Test
    public void addLike() {
        filmStorage.like(1, 1);
        assertThat(filmStorage.getTopFilms(10).size()).isEqualTo(1);
        assertThat(filmStorage.getTopFilms(10).get(0)).isEqualTo(filmStorage.get(1));
    }

    @Test
    public void deleteLike() {
        filmStorage.like(1, 1);
        assertThat(filmStorage.getTopFilms(10).size()).isEqualTo(1);
        assertThat(filmStorage.getTopFilms(10).get(0)).isEqualTo(filmStorage.get(1));

        filmStorage.disLike(1, 1);
        assertThat(filmStorage.getTopFilms(10).size()).isEqualTo(3);
    }

    @Test
    public void testMpa() {
        assertThat(mpaDbStorage.getAllMpa().size()).isEqualTo(5);
        assertThat(mpaDbStorage.getMpa(3).getName()).isEqualTo("PG-13");
    }

    @Test
    public void testGenres(){
        assertThat(genreDbStorage.getAllGenres().size()).isEqualTo(6);
        assertThat(genreDbStorage.getGenre(5).getName()).isEqualTo("Документальный");
    }

}
