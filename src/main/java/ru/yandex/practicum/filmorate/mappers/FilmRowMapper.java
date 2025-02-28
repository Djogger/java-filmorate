package ru.yandex.practicum.filmorate.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("id"));
        film.setName(rs.getString("title"));
        film.setDescription(rs.getString("description"));
        film.setDuration(rs.getInt("duration"));
        film.setReleaseDate(rs.getTimestamp("release_date").toLocalDateTime().toLocalDate());
//        String mpaRatingString = rs.getString("ratingMPA_id");
//        MpaRating mpaRating = MpaRating.valueOf(mpaRatingString); // Преобразование строки в enum
//        film.setMpaRating(mpaRating);
        return film;
    }
}
