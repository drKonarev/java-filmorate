package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.annotation.CheckForSigned;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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

    private Set<Integer> friends;

    public void addFriend(Integer id) {
        if (friends == null) friends = new HashSet<>();
        friends.add(id);
    }

}

