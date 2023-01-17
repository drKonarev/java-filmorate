package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Component
@Qualifier("memory")
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Integer, User> users = new HashMap<>();
    private int id = 0;


    private HashMap<Integer, List<Integer>> friends = new HashMap<>();

    @Override
    public void delete(Integer id) {
        check(id);
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
        check(user.getId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> get(Integer id) {
        check(id);
        return Optional.of(users.get(id));
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public List<User> getAllFriends(Integer id) {
        check(id);
        List<User> allFriends = new ArrayList<>();
        for (Integer idFriends : friends.get(id)) {
            allFriends.add(users.get(idFriends));
        }
        return allFriends;
    }

    private User create(User user) {
        String name = user.getName() == null || user.getName().isBlank() ? user.getLogin() : user.getName();

        return User.builder()
                .id(++id)
                .email(user.getEmail())
                .birthday(user.getBirthday())
                .login(user.getLogin())
                .name(name)
                .build();
    }

    private void validate(User user) {
        if (user.getLogin().contains(" ") || user.getLogin().isBlank())
            throw new ValidationException("Incorrect login format!");
    }


    @Override
    public void check(Integer id) {
        if (!users.containsKey(id))
            throw new UserNotFoundException("Not found user with such id - " + id);
    }

    @Override
    public List<User> getCommonFriends(Integer userId, Integer friendId) {
        List<User> commonFriends = new ArrayList<>();
        for (Integer id : friends.get(userId)) {

////?????????????????
        }
        return commonFriends;
    }

    @Override
    public void addFriend(Integer id, Integer friendId) {
        friends.get(id).add(friendId);
    }

    @Override
    public void deleteFriend(Integer id, Integer friendId) {
        if (Objects.equals(id, friendId)) throw new RuntimeException("You can't delete yourself from friendliest");
        if (friends.get(id).isEmpty()) throw new RuntimeException("Список друзей пуст!");
        friends.get(id).remove(friendId);
    }
}


