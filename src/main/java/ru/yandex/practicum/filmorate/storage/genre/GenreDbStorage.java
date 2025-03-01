package ru.yandex.practicum.filmorate.storage.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

@Slf4j
@Repository
public class GenreDbStorage extends BaseRepository<Genre> implements GenreStorage {
    private static final String GET_ALL_GENRES = "SELECT * FROM genres;";

    private static final String GET_GENRE = "SELECT * FROM genres WHERE genre_id = ?;";

    public GenreDbStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper, Genre.class);
    }

    public Collection<Genre> getAllGenres() {
        log.info("Вывод всех рейтингов MPA");
        return findMany(GET_ALL_GENRES);
    }

    public Optional<Genre> getGenre(int genre_id) {
        log.info("Вывод рейтинга MPA с id: " + genre_id);
        return findOne(GET_GENRE, genre_id);
    }

    public Set<Genre> getFilmGenres(long film_id) {
        log.info("Вывод рейтингов MPA фильма с id: " + film_id);
        String sqlRequest = """
                SELECT g.genre_id, name FROM films_genre AS fg
                JOIN genres AS g ON fg.genre_id = g.genre_id
                WHERE film_id = ?;
                """;
        return new TreeSet<>(super.jdbc.query(sqlRequest, (rs, rowNum) -> new Genre(
                rs.getInt("genre_id"),
                rs.getString("name")),
                film_id
        ));
    }

}
