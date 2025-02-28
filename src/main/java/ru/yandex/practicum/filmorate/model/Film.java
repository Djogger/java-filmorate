package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */
@Data
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

    private MpaRating mpaRating;

    public int getLikesCount() {
        if (likes == null) {
            likes = new HashSet<>();
        }
        return likes.size();
    }

}
