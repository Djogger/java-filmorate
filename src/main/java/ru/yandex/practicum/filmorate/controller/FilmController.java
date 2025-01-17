package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();

    private int nextId = 0;

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("Вывод всех фильмов");
        return films.values();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        validation(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Фильм с id: " + film.getId() + " успешно добавлен");
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film newFilm) {
        if (!films.containsKey(newFilm.getId())) {
            String mes = "Фильма с id: " + newFilm.getId() + " не существует";
            log.error(mes);
            throw new NotFoundException(mes);
        }
        validation(newFilm);
        films.put(newFilm.getId(), newFilm);
        log.info("Фильм с id: " + newFilm.getId() + " успешно изменён");
        return newFilm;
    }

    private void validation(Film film) {
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            String mes = "Длина описания не может превышать 200 символов";
            log.error(mes);
            throw new ValidationException(mes);
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            String mes = "Дата релиза фильма не может быть раньше 28 декабря 1895 года";
            log.error(mes);
            throw new ValidationException(mes);
        }
        if (film.getDuration() <= 0) {
            String mes = "Продолжительность фильма не может быть равна нулю или быть отрицательной";
            log.error(mes);
            throw new ValidationException(mes);
        }
    }

    private long getNextId() {
        return ++nextId;
    }

}
