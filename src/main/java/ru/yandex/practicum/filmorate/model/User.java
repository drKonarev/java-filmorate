package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@Builder
public class User {

    private int id;
    @Email
    @NonNull
    private final String email;
    @NonNull
    private final String login;
    private final String name;
    @Past
    private final LocalDate birthday;
}
