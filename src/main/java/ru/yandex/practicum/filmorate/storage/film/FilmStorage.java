package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Map;

public interface FilmStorage {
    Collection<Film> getAllFilms();

    Film addFilm(Film film);


    Film updateFilm(Film newFilm);

    Map<Long, Film> getFilms();

    Film getFilmById(Long filmId);

}
