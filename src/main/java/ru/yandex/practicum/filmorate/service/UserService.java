package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
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

        Set<Long> commonFriends = new HashSet<>(getUserById(userId).getFriends());
        commonFriends.retainAll(getUserById(friendId).getFriends());

        return commonFriends.stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    public User addUser(User user) {
        return inMemoryUserStorage.addUser(user);
    }

    public void addFriend(Long userId, Long friendId) {
        if (userId.equals(friendId)) {
            throw new IllegalArgumentException("Нельзя добавить в друзья самого себя");
        }

        User user = getUserById(userId);
        user.getFriends().add(friendId);

        User friend = getUserById(friendId);
        friend.getFriends().add(userId);

        log.info("Пользователи с id = " + userId + " и id = " + friendId + " теперь друзья");

        inMemoryUserStorage.updateUser(user);
        inMemoryUserStorage.updateUser(friend);
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

        inMemoryUserStorage.updateUser(user);
        inMemoryUserStorage.updateUser(friend);

        return user;
    }

    private User getUserById(Long userId) {
        User user = inMemoryUserStorage.getUserById(userId);

        if (user == null) {
            throw new NotFoundException("Пользователя с id = " + userId + " не найдено");
        }

        return user;
    }

}
