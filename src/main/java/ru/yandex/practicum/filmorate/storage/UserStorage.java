package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    void delete(Integer id) ;

    User post(User user) ; //add

    User put(User user) ; // update

    User get(Integer id) ;

    List<User> getAllUsers();

    List <User> getAllFriends(Integer id);

    void doesExist (Integer id);
}
