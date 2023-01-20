package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserStorage userStorage;

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void delete(Integer id) {
        userStorage.delete(id);
    }

    public Optional<User> get(Integer id) {
        return userStorage.get(id);
    }

    public User put(User user) {
        return userStorage.put(user);
    }

    public User post(User user) {
        return userStorage.post(user);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }



    public void addFriend(Integer userId, Integer friendId){
        if (userId.equals(friendId)) throw new RuntimeException ("Невозможно добавить самого себя в друзья!");
        userStorage.addFriend(userId,friendId);
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        if (userId.equals(friendId)) throw new RuntimeException ("Невозможно удалить самого себя из друзей!");
       userStorage.deleteFriend(userId, friendId);
    }



public List <User> getCommonFriends (Integer userId, Integer friendId){
        return userStorage.getCommonFriends(userId, friendId);
}
    public List<User> getFriends(Integer id) {
        return userStorage.getAllFriends(id);
    }
}
