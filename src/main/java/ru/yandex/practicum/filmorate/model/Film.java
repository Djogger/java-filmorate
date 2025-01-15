package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.Instant;
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

    LocalDate releaseDate;

    int duration;

}
