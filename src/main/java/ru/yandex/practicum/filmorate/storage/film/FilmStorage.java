package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Map;

public interface FilmStorage {
    public Collection<Film> getAllFilms();

    public Film addFilm(Film film);


    public Film updateFilm(Film newFilm);

    public Map<Long, Film> getFilms();

}
