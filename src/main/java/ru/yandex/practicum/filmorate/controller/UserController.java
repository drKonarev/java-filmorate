package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController()
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final HashMap<Integer, User> users = new HashMap<>();
    private int id = 0;

    @GetMapping()
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @PostMapping()
    public User post(@Valid @RequestBody User user) throws ValidationException {
        validate(user);
        User createdUser = create(user);
        if (users.containsKey(createdUser.getId())) {
            throw new ValidationException("User already exist!");
        }
        users.put(createdUser.getId(), createdUser);
        return createdUser;
    }

    @PutMapping()
    public User put(@Valid @RequestBody User user) throws ValidationException {
        validate(user);
        if (!(users.containsKey(user.getId()))) {
            throw new ValidationException("User doesn't exist!");
        }
        users.put(user.getId(), user);
        return user;
    }


    private void validate(User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Incorrect login format!");
        }
    }

    private User create(User user){
        User createdUser;
        if (user.getName() == null || user.getName().isBlank()) {
             createdUser =  User.builder()
                    .id(++id)
                    .email(user.getEmail())
                    .login(user.getLogin())
                    .name(user.getLogin())
                    .birthday(user.getBirthday())
                    .build();
        } else{
             createdUser= User.builder()
                    .id(++id)
                    .email(user.getEmail())
                    .login(user.getLogin())
                    .name(user.getName())
                    .birthday(user.getBirthday())
                    .build();
        }
        return createdUser;
    }
}
