package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
public class FilmDbStorage extends BaseRepository<Film> implements FilmStorage {
    private static final String GET_ALL_FILMS = "SELECT * FROM films;";
    private static final String ADD_FILM = "INSERT INTO films(title, description, duration, release_date) " +
            "VALUES (?, ?, ?, ?);";
    private static final String ADD_GENRE = "INSERT INTO films_genre(film_id, genre_id) " +
            "VALUES (?, ?);";

    private static final String DELETE_GENRE = "DELETE FROM films_genre WHERE film_id = ?;";
    private static final String UPDATE_FILM = "UPDATE films SET title = ?, description = ?, duration = ?, release_date = ?" +
            "WHERE id = ?;";
    private static final String GET_FILM_BY_ID = "SELECT * FROM films WHERE id = ?;";


    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper, Film.class);
    }

    public List<Film> getAllFilms() {
        log.info("Вывод всех фильмов");
        return findMany(GET_ALL_FILMS);
    }

    public Film addFilm(Film film) {
        long id = insert(
                ADD_FILM,
                film.getName(),
                film.getDescription(),
                film.getDuration(),
//                film.getMpaRating(),
                Timestamp.valueOf(film.getReleaseDate().atStartOfDay())
        );

//        for (Genre genre : film.getGenres()) {
//            insert(
//                    ADD_GENRE,
//                    film.getId(),
//                    genre
//            );
//        }

        log.info("Был создан фильм с id: " + id);

        film.setId(id);
        return film;
    }

    public Film updateFilm(Film newFilm) {
        update(
                UPDATE_FILM,
                newFilm.getName(),
                newFilm.getDescription(),
                newFilm.getDuration(),
//                newFilm.getGenres(),
//                newFilm.getMpaRating(),
                Timestamp.valueOf(newFilm.getReleaseDate().atStartOfDay())
        );

//        boolean deletion = delete(
//                DELETE_GENRE,
//                newFilm.getId()
//        );
//
//        if (deletion) {
//            log.info("Удаление всех жанров фильма с id: " + newFilm.getId());
//        }

//        for (Genre genre : newFilm.getGenres()) {
//            insert(
//                    ADD_GENRE,
//                    newFilm.getId(),
//                    genre
//            );
//        }

        log.info("Была обновлена информация о фильме с id: " + newFilm.getId());

        return newFilm;
    }

    public Optional<Film> getFilmById(Long filmId) {
        log.info("Вывод фильма с id: " + filmId);
        return findOne(GET_FILM_BY_ID, filmId);
    }
}
