package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

/**
 * Film.
 */
@Data
@Builder
public class Film {

    Long id;

    @NonNull
    @NotBlank
    String name;

    String description;

    @NotNull(message = "Дата релиза не может быть пустой")
    LocalDate releaseDate;

    int duration;

}
