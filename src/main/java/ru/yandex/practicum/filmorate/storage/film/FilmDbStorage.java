package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class FilmDbStorage extends BaseRepository<Film> implements FilmStorage {
    @Autowired
    private GenreStorage genreStorage;

    private static final String GET_ALL_FILMS = """
    SELECT f.id,
        f.title,
        f.description,
        f.duration,
        f.release_date,
        f.ratingMPA_id
    FROM films AS f;
""";

    private static final String GET_FILM_BY_ID = """
    SELECT f.id,
        f.title,
        f.description,
        f.duration,
        f.release_date,
        f.ratingMPA_id,
        COUNT(l.user_id) AS user_id
    FROM films AS f
    LEFT OUTER JOIN likes AS l ON l.film_id = f.id
    WHERE f.id = ?
    GROUP BY f.id, f.title, f.description, f.duration, f.release_date, f.ratingMPA_id;
""";

    private static final String ADD_FILM = """
    INSERT INTO films(title, description, duration, release_date, ratingMPA_id)
    VALUES (?, ?, ?, ?, ?);
""";

    private static final String ADD_GENRE = """
    INSERT INTO films_genre(film_id, genre_id)
    VALUES (?, ?);
""";

    private static final String DELETE_GENRE = """
    DELETE FROM films_genre 
    WHERE film_id = ?;        
""";
    private static final String UPDATE_FILM = "UPDATE films SET title = ?, description = ?, duration = ?, " +
            "release_date = ?, ratingMPA_id = ?" +
            "WHERE id = ?;";


    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper, Film.class);
    }

    public List<Film> getAllFilms() {
        log.info("Вывод всех фильмов");
        return findMany(GET_ALL_FILMS).stream()
                .peek(film -> {
                    film.setLikes(loadLikes(film.getId()));
                    film.setGenres(loadGenres(film.getId()));
                })
                .collect(Collectors.toList());
    }

    public Set<Long> loadLikes(Long film_id) {
        String sql = "SELECT user_id FROM likes WHERE film_id = ?";
        return new HashSet<>(jdbc.query(sql, new Object[]{film_id}, (rs, rowNum) -> rs.getLong("user_id")));
    }

    public Set<Genre> loadGenres(Long film_id) {
        String sql = "SELECT genre_id FROM films_genre WHERE film_id = ?";
        Set<Integer> genresId = new HashSet<>(jdbc.query(sql, new Object[]{film_id}, (rs, rowNum) -> rs.getInt("genre_id")));
        return genresId.stream()
                .map(id -> genreStorage.getGenre(id).get())
                .collect(Collectors.toSet());
    }

    public Film addFilm(Film film) {
        for (Genre genre : film.getGenres()) {
            if (genre.getId() > 6) {
                throw new NotFoundException("Не найден жанр с id: " + film.getMpa().getId());
            }
        }

        if (film.getMpa().getId() > 5) {
            throw new NotFoundException("Не найден рейтинг MPA с id: " + film.getMpa().getId());
        }

        if (film.getReleaseDate().getYear() < 1895) {
            throw new ValidationException("Первый фильм был создан не раньше 1895 года");
        }

        long id = insert(
                ADD_FILM,
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                Timestamp.valueOf(film.getReleaseDate().atStartOfDay()),
                film.getMpa().getId()
        );

        for (Genre genre : film.getGenres()) {
            insert(ADD_GENRE, id, genre.getId());
        }

        log.info("Был создан фильм с id: " + id + " его информация: " + getFilmById(id));
        film.setId(id);
        return film;
    }

    public Film updateFilm(Film newFilm) {
        int rowsUpdated = update(
                UPDATE_FILM,
                newFilm.getName(),
                newFilm.getDescription(),
                newFilm.getDuration(),
                Timestamp.valueOf(newFilm.getReleaseDate().atStartOfDay()),
                newFilm.getMpa().getId(),
                newFilm.getId()
        );

        if (rowsUpdated == 0) {
            throw new NotFoundException("Фильм с id: " + newFilm.getId() + " не найден");
        }

        boolean deletion = delete(
                DELETE_GENRE,
                newFilm.getId()
        );

        if (deletion) {
            log.info("Удаление всех жанров фильма с id: " + newFilm.getId());
        }

        for (Genre genre : newFilm.getGenres()) {
            insert(
                    ADD_GENRE,
                    newFilm.getId(),
                    genre.getId()
            );
        }

        log.info("Была обновлена информация о фильме с id: " + newFilm.getId());

        return newFilm;
    }

    public void addLike(Long film_id, Long user_id) {
        String sql = "INSERT INTO likes(user_id, film_id) VALUES (?, ?);";
        int changedRows = jdbc.update(sql, user_id, film_id);
        if (changedRows > 0) {
            log.info("Лайк успешно добавлен: user_id = " + user_id + ", film_id = " + film_id);
        } else {
            throw new NotFoundException("Не удалось добавить лайк: user_id = " + user_id + ", film_id = " + film_id);
        }
    }

    public void deleteLike(Long film_id, Long user_id) {
        String sql = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        int changedRows = jdbc.update(sql, user_id, film_id);
        if (changedRows > 0) {
            log.info("Лайк успешно удалён: user_id = " + user_id + ", film_id = " + film_id);
        }
    }

    public Optional<Film> getFilmById(Long filmId) {
        log.info("Вывод фильма с id: " + filmId);
        return findOne(GET_FILM_BY_ID, filmId).stream()
                .peek(film -> {
                    film.setLikes(loadLikes(film.getId()));
                    film.setGenres(loadGenres(film.getId()));
                })
                .findFirst();
    }
}
