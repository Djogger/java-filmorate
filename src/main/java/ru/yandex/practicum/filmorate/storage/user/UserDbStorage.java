package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
public class UserDbStorage extends BaseRepository<User> implements UserStorage {
    private static final String GET_ALL_USERS = "SELECT * FROM users;";
    private static final String ADD_USER = "INSERT INTO users(name, login, email, birthday) " +
            "VALUES (?, ?, ?, ?);";
    private static final String UPDATE_USER = "UPDATE users SET name = ?, login = ?, email = ?, birthday = ? " +
            "WHERE id = ?;";
    private static final String GET_USER_BY_ID = "SELECT * FROM users WHERE id = ?;";


    public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper, User.class);
    }

    public List<User> getAllUsers() {
        log.info("Вывод всех пользователей");
        return findMany(GET_ALL_USERS);
    }

    public User addUser(User user) {
        long id = insert(
                ADD_USER,
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                Timestamp.valueOf(user.getBirthday().atStartOfDay())
        );

        log.info("Был создан пользователь с id: " + id);

        user.setId(id);
        return user;
    }

    public User updateUser(User newUser) {
        // Проверяем, что объект newUser и его поля не равны null
        if (newUser == null) {
            throw new IllegalArgumentException("Объект newUser не может быть null");
        }
        if (newUser.getId() == null) {
            throw new IllegalArgumentException("ID пользователя не может быть null");
        }
        if (newUser.getName() == null || newUser.getLogin() == null || newUser.getEmail() == null || newUser.getBirthday() == null) {
            throw new IllegalArgumentException("Поля name, login, email и birthday не могут быть null");
        }

        // Выполняем обновление
        try {
            update(
                    UPDATE_USER,
                    newUser.getName(),
                    newUser.getLogin(),
                    newUser.getEmail(),
                    Timestamp.valueOf(newUser.getBirthday().atStartOfDay()),
                    newUser.getId()
            );

            log.info("Была обновлена информация о пользователе с id: " + newUser.getId());
        } catch (InternalServerException ex) {
            log.error("Ошибка при обновлении пользователя с id {}: {}", newUser.getId(), ex.getMessage());
            throw new InternalServerException("Не удалось обновить данные пользователя");
        }

        return newUser;
    }

    public Optional<User> getUserById(Long userId) {
        log.info("Вывод пользователя с id: " + userId);
        return findOne(GET_USER_BY_ID, userId);
    }
}
