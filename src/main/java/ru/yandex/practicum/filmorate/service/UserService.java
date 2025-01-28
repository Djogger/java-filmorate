package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {
    private final UserStorage inMemoryUserStorage;

    public Collection<Map<String, Long>> findFriends(Long userId) {
        validation(userId);

        return wrapperResponse(inMemoryUserStorage.getUsers().get(userId).getFriends());
    }

    public Collection<Map<String, Long>> findCommonFriends(Long userId, Long friendId) {
        log.info("Находим общих друзей у пользователей с id = " + userId + " и id = " + friendId);
        validation(userId);
        validation(friendId);

        Set<Long> commonFriends = new HashSet<>(inMemoryUserStorage.getUsers().get(userId).getFriends());
        commonFriends.retainAll(inMemoryUserStorage.getUsers().get(friendId).getFriends());

        return wrapperResponse(commonFriends);
    }

    public Collection<Map<String, Long>> addFriend(Long userId, Long friendId) {
        if (userId.equals(friendId)) {
            throw new IllegalArgumentException("Нельзя добавить в друзья самого себя");
        }

        log.info("Пользователи с id = " + userId + " и id = " + friendId + " теперь друзья");
        validation(userId);
        validation(friendId);

        User user = inMemoryUserStorage.getUsers().get(userId);
        user.getFriends().add(friendId);
        User friend = inMemoryUserStorage.getUsers().get(friendId);
        friend.getFriends().add(userId);

        inMemoryUserStorage.updateUser(user);
        inMemoryUserStorage.updateUser(friend);

        return wrapperResponse(user.getFriends());
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

    private void validation(Long id) {
        if (inMemoryUserStorage.getUsers().get(id) == null) {
            throw new NotFoundException("Пользователя с таким id не найдено");
        }
    }

    private Collection<Map<String, Long>> wrapperResponse(Set<Long> friends) {
        Collection<Map<String, Long>> response = new ArrayList<>();
        for (Long id : friends) {
            Map<String, Long> map = new HashMap<>();
            map.put("id", id);
            response.add(map);
        }

        return response;
    }

}
