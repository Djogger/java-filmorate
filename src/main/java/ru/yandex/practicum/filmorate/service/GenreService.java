package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class GenreService {
    private final GenreDbStorage inMemoryGenreStorage;

    public Collection<Genre> getAllGenres() {
        return inMemoryGenreStorage.getAllGenres();
    }

    public Genre getGenre(int genre_id) {
        Optional<Genre> genre = inMemoryGenreStorage.getGenre(genre_id);

        if (genre.isEmpty()) {
            throw new NotFoundException("Genre с id = " + genre_id + " не найдено");
        }

        return genre.get();
    }
}
