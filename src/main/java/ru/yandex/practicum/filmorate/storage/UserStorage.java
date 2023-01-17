package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    void delete(Integer id);

    User post(User user); //add

    User put(User user); // update

    Optional<User> get(Integer id);

    List<User> getAllUsers();

    List<User> getAllFriends(Integer id);

    List<User> getCommonFriends(Integer userId, Integer friendId);

    void addFriend(Integer userId, Integer friendId);

    void deleteFriend(Integer userId, Integer friendId);

    void check (Integer id);

}
