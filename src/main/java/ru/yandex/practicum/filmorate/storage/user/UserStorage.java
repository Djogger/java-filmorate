package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;

public interface UserStorage {
    public Collection<User> getAllUsers();

    public User addUser(User user);

    public User updateUser(User newUser);

    public Map<Long, User> getUsers();

}
