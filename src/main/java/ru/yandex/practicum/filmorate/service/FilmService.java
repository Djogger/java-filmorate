package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class FilmService {
    private final FilmStorage inMemoryFilmStorage;
    private final UserStorage inMemoryUserStorage;

    public Collection<Film> findPopularFilms(int count) {
        log.info("Возвращаем " + count + " самых популярных фильмов");
        return inMemoryFilmStorage.getFilms().values().stream()
                .sorted(Comparator.comparing(Film::getLikesCount).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film addLike(Long filmId, Long userId) {
        log.info("Фильм с id = " + filmId + " понравился пользователю с id = " + userId);
        validation(filmId, userId);

        Film film = inMemoryFilmStorage.getFilms().get(filmId);
        film.getLikes().add(userId);

        inMemoryFilmStorage.updateFilm(film);

        return film;
    }

    public Film deleteLike(Long filmId, Long userId) {
        log.info("Фильм с id = " + filmId + " лишился отметки \"Нравится\" пользователя с id = " + userId);
        validation(filmId, userId);

        Film film = inMemoryFilmStorage.getFilms().get(filmId);
        film.getLikes().remove(userId);

        inMemoryFilmStorage.updateFilm(film);

        return film;
    }

    public void validation(Long filmId, Long userId) {
        if (inMemoryFilmStorage.getFilms().get(filmId) == null || inMemoryUserStorage.getUsers().get(userId) == null) {
            throw new NotFoundException("Указаны несуществующие id");
        }
    }

}
