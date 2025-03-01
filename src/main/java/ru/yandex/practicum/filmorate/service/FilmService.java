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

    public Collection<Film> getAllFilms() {
        return inMemoryFilmStorage.getAllFilms();
    }

    public Film getFilm(Long filmId) {
        Optional<Film> film = inMemoryFilmStorage.getFilmById(filmId);

        if (film.isEmpty()) {
            throw new NotFoundException("Фильма с id = " + filmId + " не найдено");
        }

        return film.get();
    }

    public Collection<Film> findPopularFilms(int count) {
        log.info("Возвращаем " + count + " самых популярных фильмов");
        return inMemoryFilmStorage.getAllFilms().stream()
                .sorted(Comparator.comparing(Film::getLikesCount).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film addFilm(Film film) {
        return inMemoryFilmStorage.addFilm(film);
    }

    public Film addLike(Long filmId, Long userId) {
        Optional<Film> film = inMemoryFilmStorage.getFilmById(filmId);

        inMemoryUserStorage.getUserById(userId);

        if (film.isPresent()) {
            Film updatedFilm = film.get();

            updatedFilm.getLikes().add(userId);

            log.info("Фильм с id = " + filmId + " понравился пользователю с id = " + userId);

            inMemoryFilmStorage.updateFilm(updatedFilm);
            inMemoryFilmStorage.addLike(filmId, userId);

            return updatedFilm;
        }

        throw new NotFoundException("Фильм с id: " + filmId + " не найден.");

    }

    public Film updateFilm(Film newFilm) {
        return inMemoryFilmStorage.updateFilm(newFilm);
    }

    public Film deleteLike(Long filmId, Long userId) {
        Optional<Film> film = inMemoryFilmStorage.getFilmById(filmId);

        inMemoryUserStorage.getUserById(userId);

        if (film.isPresent()) {
            Film updatedFilm = film.get();
            updatedFilm.getLikes().remove(userId);

            log.info("Фильм с id = " + filmId + " лишился отметки \"Нравится\" пользователя с id = " + userId);

            inMemoryFilmStorage.updateFilm(updatedFilm);
            inMemoryFilmStorage.deleteLike(filmId, userId);

            return updatedFilm;
        }

        throw new NotFoundException("Фильм с id: " + filmId + " не найден.");

    }

}
