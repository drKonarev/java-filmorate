package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Integer, User> users = new HashMap<>();
    private int id = 0;

    @Override
    public void delete(Integer id) {
        doesExist(id);
        users.remove(id);
    }


    @Override
    public User post(User user) {
        validate(user);
        User createdUser = create(user);
        if (users.containsKey(user.getId())) {
            throw new UserAlreadyExistException("User already exist!");
        }
        users.put(createdUser.getId(), createdUser);
        return createdUser;
    }

    @Override
    public User put(User user) {
        validate(user);
        doesExist(user.getId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User get(Integer id) {
        doesExist(id);
        return users.get(id);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public List<User> getAllFriends(Integer id) {
        doesExist(id);
        List<User> friends = new ArrayList<>();
        for (Integer idFriends : users.get(id).getFriends()) {
            friends.add(users.get(idFriends));
        }
        return friends;
    }

    private User create(User user) {
        User createdUser;
        if (user.getName() == null || user.getName().isBlank()) {
            createdUser = User.builder()
                    .id(++id)
                    .email(user.getEmail())
                    .login(user.getLogin())
                    .name(user.getLogin())
                    .birthday(user.getBirthday())
                    .friends(new HashSet<>())
                    .build();
        } else {
            createdUser = User.builder()
                    .id(++id)
                    .email(user.getEmail())
                    .login(user.getLogin())
                    .name(user.getName())
                    .birthday(user.getBirthday())
                    .friends(new HashSet<>())
                    .build();
        }
        return createdUser;
    }

    private void validate(User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Incorrect login format!");
        }
    }


    @Override
    public void doesExist(Integer id) {
        if (!users.containsKey(id)) throw new UserNotFoundException("Not found user with such id - " + id);
    }
}
