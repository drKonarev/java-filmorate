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

import java.time.LocalDate;

@SpringBootTest
class FilmorateApplicationTests {

    UserController userCont;
    FilmController filmCont;

    @BeforeEach
    void before() {
        userCont = new UserController();
        filmCont = new FilmController();
    }


    @Test
    void loginWithSpace() {
        User test = User.builder()
                .name("name")
                .email("email@mail.ru")
                .login("wrong login")
                .birthday(LocalDate.of(1997, 5, 14))
                .build();

        Assertions.assertThrows(ValidationException.class, () -> userCont.testValidate(test));
    }

    @Test
    void nullName() {
        User test = User.builder()
                .name(null)
                .email("email@mail.ru")
                .login("login")
                .birthday(LocalDate.of(1997, 5, 14))
                .build();

        Assertions.assertEquals(userCont.testCreate(test).getName(), "login");
    }

    @Test
    void blankUserName() {
        User test = User.builder()
                .name("")
                .email("email@mail.ru")
                .login("login")
                .birthday(LocalDate.of(1997, 5, 14))
                .build();

        Assertions.assertEquals(userCont.testCreate(test).getName(), "login");
    }

    @Test
    void blankFilmName() {
        Film test = Film.builder()
                .description("description")
                .name("")
                .releaseDate(LocalDate.of(1997, 5, 14))
                .duration(100)
                .build();

        Assertions.assertThrows(ValidationException.class, () -> filmCont.test(test));
    }

    @Test
    void incorrectReleaseDate() {
        Film test = Film.builder()
                .description("description")
                .name("name")
                .releaseDate(LocalDate.of(1167, 5, 14))
                .duration(100)
                .build();

        Assertions.assertThrows(ValidationException.class, () -> filmCont.test(test));
    }


}
