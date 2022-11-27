package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class Film {

    private int id;
    @NonNull
    private final String name;
    @Size(min = 1, max = 200)
    @NonNull
    private final String description;
    @Past
    @NonNull
    private final LocalDate releaseDate;
    @Positive
    private final int duration;

    private Set<Integer> likes;


}
