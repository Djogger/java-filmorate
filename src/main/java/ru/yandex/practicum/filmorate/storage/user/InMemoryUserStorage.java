package ru.yandex.practicum.filmorate.storage.user;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    @Getter
    private final Map<Long, User> users = new HashMap<>();

    private int nextId = 0;

    @Override
    public Collection<User> getAllUsers() {
        log.info("Вывод всех пользователей");
        return users.values();
    }

    @Override
    public User addUser(User user) {
        validation(user);
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Юзер с id: " + user.getId() + " успешно добавлен");
        return user;
    }

    @Override
    public User updateUser(User newUser) {
        if (!users.containsKey(newUser.getId())) {
            String mes = "Пользователя с id: " + newUser.getId() + " не существует";
            log.error(mes);
            throw new NotFoundException(mes);
        }
        validation(newUser);
        users.put(newUser.getId(), newUser);
        log.info("Юзер с id: " + newUser.getId() + " успешно изменён");
        return newUser;
    }

    private void validation(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.warn("У пользователя отсутствует имя, вместо имени будет использоваться логин: " + user.getLogin());
            user.setName(user.getLogin());
        }
        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }
        if (user.getBirthday().atStartOfDay(ZoneId.of("UTC")).toInstant().isAfter(Instant.now())) {
            String mes = "Дата рождения не может быть в будущем";
            log.error(mes);
            throw new ValidationException(mes);
        }
    }

    public User getUserById(Long userId) {
        User user = users.get(userId);

        if (user == null) {
            throw new NotFoundException("Пользователя с id = " + userId + " не существует");
        }

        return user;
    }

    private long getNextId() {
        return ++nextId;
    }

}
