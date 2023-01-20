package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.ValidationException;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@Builder
public class User {

    private int id;
    @Email(message = "Non-valid email data!")
    @NonNull
    private final String email;
    @NonNull
    private final String login;

    private final String name;
    @Past (message = "Birthday must be a past date!")
    private final LocalDate birthday;


}

