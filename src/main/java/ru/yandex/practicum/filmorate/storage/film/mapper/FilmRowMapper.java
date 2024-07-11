package ru.yandex.practicum.filmorate.storage.film.mapper;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film f = new Film();

        f.setId(rs.getLong("id"));
        f.setName(rs.getString("name"));
        f.setDescription(rs.getString("description"));
        f.setReleaseDate(
                LocalDate.parse(rs.getString("release_date"), DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        );
        f.setDuration(Duration.ofMinutes(rs.getLong("duration")));

        long ratingId = rs.getLong("rating_id");
        if (ratingId != 0) {
            Rating rating = new Rating();
            rating.setId(ratingId);
            rating.setName(rs.getString("rating_name"));
            f.setMpa(rating);
        }

        do {
            long likeId = rs.getLong("user_id");
            if (likeId != 0) {
                f.getLikes().add(likeId);
            }

            long genreId = rs.getLong("genre_id");
            if (genreId != 0) {
                Genre genre = new Genre();
                genre.setId(genreId);
                genre.setName(rs.getString("genre_name"));
                f.getGenres().add(genre);
            }
        } while (rs.next());

        return f;
    }
}
