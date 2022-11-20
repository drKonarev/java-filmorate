package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController()
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping(value = "/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") Integer id, @PathVariable("friendId") Integer friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping(value = "/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") Integer id, @PathVariable("friendId") Integer friendId) {
        userService.deleteFriend(id, friendId);
    }

    @GetMapping(value = "/{id}/friends")
    public List<User> friends(@PathVariable("id") Integer id) {
        return userService.getFriends(id);
    }

    @GetMapping(value = "/{id}/friends/common/{otherId}")
    public List<User> commonFriends(@PathVariable("id") Integer id,
                                    @PathVariable("otherId") Integer otherId) {
        return userService.commonFriends(id, otherId);
    }

    @GetMapping()
    public List<User> findAll() {
        return userService.getAllUsers();
    }

    @GetMapping(value = "/{id}")
    public User get(@PathVariable("id") Integer id) {
        return userService.get(id);
    }

    @PostMapping()
    public User post(@Valid @RequestBody User user) throws ValidationException {

        return userService.post(user);

    }


    @PutMapping()
    public User put(@Valid @RequestBody User user) throws ValidationException {

        return userService.put(user);
    }

    @DeleteMapping(path = "{id}")
    public void delete(@PathVariable("id") int userId) {
        userService.delete(userId);
    }


}
