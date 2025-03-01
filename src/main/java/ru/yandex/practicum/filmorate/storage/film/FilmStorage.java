package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FilmStorage {
    List<Film> getAllFilms();

    Set<Long> loadLikes(Long film_id);

    Film addFilm(Film film);

    Film updateFilm(Film newFilm);

    void addLike(Long film_id, Long user_id);

    void deleteLike(Long film_id, Long user_id);

    Optional<Film> getFilmById(Long filmId);

}
