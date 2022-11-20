package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void delete(Integer id) {
        userStorage.delete(id);
    }

    public User get(Integer id) {
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

    public void addFriend(Integer id, Integer friendId) {
        if (id==friendId) throw new RuntimeException("You can't add yourself to friendList");
        User user = userStorage.get(id);
        User friend = userStorage.get(friendId);
        user.addFriend(friendId);
        friend.addFriend(id);
    }

    public void deleteFriend(Integer id, Integer friendId) {
        User user = userStorage.get(id);
        User friend = userStorage.get(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(id);

    }

    public List<User> commonFriends(Integer firstId, Integer secondId) {
        User first = userStorage.get(firstId);
        User second = userStorage.get(secondId);
        List<User> commonFriends = new ArrayList<>();
        for (Integer id : second.getFriends()) {
            if (first.getFriends().contains(id)) {
                commonFriends.add(userStorage.get(id));
            }
        }
        return commonFriends;
    }


    public List<User> getFriends(Integer id) {
        return userStorage.getAllFriends(id);
    }
}
