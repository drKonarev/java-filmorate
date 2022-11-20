package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

@SpringBootTest
class FilmorateApplicationTests {

    UserController userCont;
    FilmController filmCont;

    @BeforeEach
    void before() {
        userCont = new UserController(new UserService(new InMemoryUserStorage()));
        filmCont = new FilmController(new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage()));
    }


    @Test
    void loginWithSpace() {

        User test = User.builder()
                .name("name")
                .email("email@mail.ru")
                .login("wrong login")
                .birthday(LocalDate.of(1997, 5, 14))
                .build();

        Assertions.assertThrows(ValidationException.class, () -> userCont.post(test));
    }

    @Test
    void nullName() {
        User test = User.builder()
                .name(null)
                .email("email@mail.ru")
                .login("login")
                .birthday(LocalDate.of(1997, 5, 14))
                .build();

        Assertions.assertEquals(userCont.post(test).getName(), "login");
    }

    @Test
    void blankUserName() {
        User test = User.builder()
                .name("")
                .email("email@mail.ru")
                .login("login")
                .birthday(LocalDate.of(1997, 5, 14))
                .build();

        Assertions.assertEquals(userCont.post(test).getName(), "login");
    }

    @Test
    void updateWrongUser() {
        User test = User.builder()
                .name("name")
                .email("email@mail.ru")
                .login("login")
                .birthday(LocalDate.of(1997, 5, 14))
                .build();
        userCont.post(test);
        Assertions.assertEquals(userCont.findAll().size(), 1);
        User otherTest = User.builder()
                .name("otherName")
                .email("otherEmail@mail.ru")
                .login("otherLogin")
                .id(5)
                .birthday(LocalDate.of(1997, 5, 14))
                .build();
        Assertions.assertThrows(ValidationException.class, () -> userCont.put(otherTest));
        Assertions.assertEquals(userCont.findAll().get(0).getName(), "name");
    }

    @Test
    void deleteWrongUser() {
        User test = User.builder()
                .name("name")
                .email("email@mail.ru")
                .login("login")
                .birthday(LocalDate.of(1997, 5, 14))
                .build();
        userCont.post(test);
        Assertions.assertEquals(userCont.findAll().size(), 1);
        Assertions.assertThrows(ValidationException.class, () -> userCont.delete(5));
        Assertions.assertEquals(userCont.findAll().size(), 1);
    }

    @Test
    void blankFilmName() {
        Film test = Film.builder()
                .description("description")
                .name("")
                .releaseDate(LocalDate.of(1997, 5, 14))
                .duration(100)
                .build();

        Assertions.assertThrows(ValidationException.class, () -> filmCont.post(test));
    }

    @Test
    void incorrectReleaseDate() {
        Film test = Film.builder()
                .description("description")
                .name("name")
                .releaseDate(LocalDate.of(1167, 5, 14))
                .duration(100)
                .build();

        Assertions.assertThrows(ValidationException.class, () -> filmCont.post(test));
    }

    @Test
    void deleteWrongFilm() {
        Film film = Film.builder()
                .description("description")
                .name("name")
                .releaseDate(LocalDate.of(1967, 5, 14))
                .duration(100)
                .build();
        filmCont.post(film);
        Assertions.assertEquals(filmCont.findAll().size(), 1);
        Assertions.assertThrows(ValidationException.class, () -> filmCont.delete(2));
        Assertions.assertEquals(filmCont.findAll().size(), 1);
    }

    @Test
    void updateWrongFilm() {
        Film film = Film.builder()
                .description("description")
                .name("name")
                .releaseDate(LocalDate.of(1967, 5, 14))
                .duration(100)
                .build();
        filmCont.post(film);
        Assertions.assertEquals(filmCont.findAll().size(), 1);

        Film test = Film.builder()
                .description("changedDescription")
                .name("changedName")
                .id(2)
                .releaseDate(LocalDate.of(1967, 5, 14))
                .duration(100)
                .build();

        Assertions.assertThrows(ValidationException.class, () -> filmCont.put(test));
    }
}