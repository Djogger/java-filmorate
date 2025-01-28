package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

/**
 * Film.
 */
@Data
@Builder
public class Film {

    Long id;

    @NotNull(message = "Поле name не может быть пустым")
    @NotBlank(message = "Поле name не может быть пустым")
    String name;

    String description;

    @NotNull(message = "Дата релиза не может быть пустой")
    LocalDate releaseDate;

    int duration;

    Set<Long> likes;

    public int getLikesCount() {
        return likes.size();
    }

}
