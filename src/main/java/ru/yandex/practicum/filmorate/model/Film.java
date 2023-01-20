package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Builder
public class Film {

    private int id;

    @NonNull
    @NotBlank(message = "Имя не может быть пустым!")
    private final String name;

    @Size(min = 1, max = 200)
    @NonNull
    private final String description;

    @Past
    @NonNull
    private final LocalDate releaseDate;

    @Positive(message = "Длительность не может быть меньше 0!")
    private final int duration;

    private final Mpa mpa;

    private final Set<Genre> genres = new LinkedHashSet<>();


}
