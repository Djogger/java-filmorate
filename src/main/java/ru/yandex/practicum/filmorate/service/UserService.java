package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {
    private final UserStorage inMemoryUserStorage;

    public Collection<User> getAllUsers() {
        return inMemoryUserStorage.getAllUsers();
    }

    public Collection<User> findFriends(Long userId) {
        return getUserById(userId).getFriends().stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    public Collection<User> findCommonFriends(Long userId, Long friendId) {
        log.info("Находим общих друзей у пользователей с id = " + userId + " и id = " + friendId);
        return inMemoryUserStorage.findCommonFriends(userId, friendId);
    }

    public User addUser(User user) {
        return inMemoryUserStorage.addUser(user);
    }

    public User addFriend(Long userId, Long friendId) {
        if (userId.equals(friendId)) {
            throw new IllegalArgumentException("Нельзя добавить в друзья самого себя");
        }

        inMemoryUserStorage.getUserById(userId);
        inMemoryUserStorage.getUserById(friendId);

        User user = getUserById(userId);
        user.getFriends().add(friendId);

        log.info("Пользователи с id = " + userId + " добавил в друзья пользователя с id = " + friendId);
        log.info("user: " + user.getFriends());

        return inMemoryUserStorage.updateUser(user);
    }

    public User updateUser(User newUser) {
        return inMemoryUserStorage.updateUser(newUser);
    }

    public User deleteFriend(Long userId, Long friendId) {
        User user = getUserById(userId);
        user.getFriends().remove(friendId);

        User friend = getUserById(friendId);
        friend.getFriends().remove(userId);

        log.info("Пользователи с id = " + userId + " и id = " + friendId + " больше не являются друзьями");

        inMemoryUserStorage.deleteFriend(userId, friendId);

        inMemoryUserStorage.updateUser(user);
        inMemoryUserStorage.updateUser(friend);

        return user;
    }

    private User getUserById(Long userId) {
        return inMemoryUserStorage.getUserById(userId);
    }

}
