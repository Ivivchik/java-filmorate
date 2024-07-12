package ru.yandex.practicum.filmorate.storage.film.extractor;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FilmResultSetExtractor implements ResultSetExtractor<List<Film>> {
    @Override
    public List<Film> extractData(ResultSet rs) throws SQLException, DataAccessException {

        Map<Long, Film> films = new HashMap<>();

        while (rs.next()) {
            Long id = rs.getLong("id");
            Film film = films.get(id);

            if (film == null) {
                Film f = new Film();
                f.setId(id);
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
                films.put(id, f);
            } else {
                long likeId = rs.getLong("user_id");
                if (likeId != 0) {
                    film.getLikes().add(likeId);
                }

                long genreId = rs.getLong("genre_id");
                if (genreId != 0) {
                    Genre genre = new Genre();
                    genre.setId(genreId);
                    genre.setName(rs.getString("genre_name"));
                    film.getGenres().add(genre);
                }
            }
        }

        return new ArrayList<>(films.values());
    }
}
