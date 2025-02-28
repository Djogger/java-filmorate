package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
public class User {

    Long id;

    @NotNull(message = "Поле email не может быть пустым")
    @Email(message = "Значение в поле email не соответствует формату почты")
    String email;

    @NotNull(message = "Поле login не может быть пустым")
    @NotBlank(message = "Поле login не может быть пустым")
    String login;

    String name;

    @NotNull(message = "Поле дня рождения не может быть пустым")
    LocalDate birthday;

    Set<Long> friends;

    private Map<Long, FriendshipStatus> friendshipStatuses = new HashMap<>();

}
