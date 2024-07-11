package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;
import ru.yandex.practicum.filmorate.utils.Tuple2;
import ru.yandex.practicum.filmorate.utils.exception.NotFoundException;
import ru.yandex.practicum.filmorate.utils.exception.ConditionsNotMetException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.*;

@Slf4j
@Repository("filmDbStorage")
public class FilmDbStorage extends BaseDbStorage<Film> {

    private static final String FIND_ALL_QUERY = "SELECT " +
            "f.id,f.name, f.description, f.release_date, f.duration, f.rating_id, " +
            "r.name as rating_name, ulf.user_id, fg.genre_id, g.name as genre_name " +
            "FROM films f " +
            "LEFT JOIN user_like_film ulf ON f.id = ulf.film_id " +
            "LEFT JOIN film_genre fg ON f.id = fg.film_id " +
            "LEFT JOIN genre g ON g.id = fg.genre_id " +
            "LEFT JOIN rating r ON r.id = f.rating_id";
    private static final String FIND_BY_ID_QUERY = FIND_ALL_QUERY + " WHERE f.id = ? ORDER BY fg.genre_id";
    private static final String ADD_FILM_GENRE_LINK = "INSERT INTO film_genre(film_id, genre_id) VALUES (?, ?)";

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper, ResultSetExtractor<List<Film>> extractor) {
        super(jdbc, mapper, extractor);
    }

    @Override
    public Collection<Film> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Film create(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbc)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> m = filmToMap(film);

        Long id = simpleJdbcInsert.executeAndReturnKey(m).longValue();
        film.setId(id);

        log.info("Film id=" + film.getId() + " successfully added");

        insertFilmGenreLink(film.getGenres(), id);

        return findById(film.getId());
    }

    @Override
    public Film update(Film newFilm) {
        Long filmId = newFilm.getId();

        if (!isExists(filmId, "films")) {
            log.error("Unable to update film data. Id: " + filmId + " doesn't exist");
            throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        }

        Tuple2<String, Object[]> queryAndParams = createUpdateQueryWithParams(newFilm);

        if (update(queryAndParams.getFirst(), queryAndParams.getSecond())) {
            log.info("Film data id=" + filmId + " successfully updated");
        }

        return findById(filmId);
    }

    @Override
    public Film findById(Long id) {
        Optional<Film> filmOpt = findOne(FIND_BY_ID_QUERY, id);
        if (filmOpt.isEmpty()) {
            log.error("Film id: " + id + " doesn't exist");
            throw new NotFoundException("Film с id = " + id + " не найден");
        }

        return filmOpt.get();
    }

    private Tuple2<String, Object[]> createUpdateQueryWithParams(Film newFilm) {
        StringBuilder query = new StringBuilder("UPDATE films SET ");
        List<Object> parameters = new ArrayList<>();

        Optional.ofNullable(newFilm.getName()).ifPresent(name -> {
            parameters.add(name);
            query.append("name = ?, ");
        });
        Optional.ofNullable(newFilm.getDescription()).ifPresent(description -> {
            parameters.add(description);
            query.append("description = ?, ");
        });
        Optional.ofNullable(newFilm.getReleaseDate()).ifPresent(releaseDate -> {
            parameters.add(releaseDate);
            query.append("release_date = ?, ");
        });
        Optional.ofNullable(newFilm.getDuration()).ifPresent(duration -> {
            parameters.add(duration.toMinutes());
            query.append("duration = ?, ");
        });

        query.delete(query.length() - 2, query.length());
        query.append(" WHERE id = ?");
        parameters.add(newFilm.getId());

        return new Tuple2<>(query.toString(), parameters.toArray());
    }

    private Map<String, Object> filmToMap(Film film) {
        Map<String, Object> m = new HashMap<>();
        m.put("name", film.getName());
        m.put("description", film.getDescription());
        m.put("release_date", film.getReleaseDate());
        m.put("duration", film.getDuration().toMinutes());

        if (!isExists(film.getMpa().getId(), "rating")) {
            log.error("Unable to find rating. Id: " + film.getMpa().getId() + " doesn't exist");
            throw new ConditionsNotMetException("Рейтинг с id = " + film.getMpa().getId() + " не найден");
        }
        m.put("rating_id", film.getMpa().getId());

        return m;
    }

    private void insertFilmGenreLink(Set<Genre> genres, Long filmId) {
        genres.forEach(genre -> {
            if (isExists(genre.getId(), "genre")) {
                jdbc.update(ADD_FILM_GENRE_LINK, filmId, genre.getId());
                log.info("Film id=" + filmId + " with genre id=" + genre.getId() + " successfully added");
            } else {
                log.error("Unable to find genre. Id: " + genre.getId() + " doesn't exist");
                throw new ConditionsNotMetException("Жанр с id = " + genre.getId() + " не найден");
            }
        });
    }
}
