package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {
    private final UserStorage inMemoryUserStorage;

    public Collection<Long> findFriends(Long userId) {
        validation(userId);
        return inMemoryUserStorage.getUsers().get(userId).getFriends();
    }

    public Collection<Long> findCommonFriends(Long userId, Long friendId) {
        log.info("Находим общих друзей у пользователей с id = " + userId + " и id = " + friendId);
        validation(userId);
        validation(friendId);

        Set<Long> commonFriends = new HashSet<>(inMemoryUserStorage.getUsers().get(userId).getFriends());
        commonFriends.retainAll(inMemoryUserStorage.getUsers().get(friendId).getFriends());
        return commonFriends;
    }

    public Collection<Long> addFriend(Long userId, Long friendId) {
        log.info("Пользователи с id = " + userId + " и id = " + friendId + " теперь друзья");
        validation(userId);
        validation(friendId);

        User user = inMemoryUserStorage.getUsers().get(userId);
        user.getFriends().add(friendId);
        User friend = inMemoryUserStorage.getUsers().get(friendId);
        friend.getFriends().add(userId);

        inMemoryUserStorage.updateUser(user);
        inMemoryUserStorage.updateUser(friend);

        return user.getFriends();
    }

    public User deleteFriend(Long userId, Long friendId) {
        log.info("Пользователи с id = " + userId + " и id = " + friendId + " больше не являются друзьями");
        validation(userId);
        validation(friendId);

        User user = inMemoryUserStorage.getUsers().get(userId);
        user.getFriends().remove(friendId);
        User friend = inMemoryUserStorage.getUsers().get(friendId);
        friend.getFriends().remove(userId);

        inMemoryUserStorage.updateUser(user);
        inMemoryUserStorage.updateUser(friend);

        return user;
    }

    public void validation(Long id) {
        if (inMemoryUserStorage.getUsers().get(id) == null) {
            throw new NotFoundException("Пользователя с таким id не найдено");
        }
    }

}
