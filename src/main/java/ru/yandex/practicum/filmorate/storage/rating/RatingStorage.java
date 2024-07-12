package ru.yandex.practicum.filmorate.storage.rating;

import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;
import ru.yandex.practicum.filmorate.utils.exception.NotFoundException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository("ratingStorage")
public class RatingStorage extends BaseDbStorage<Rating> {

    private static final String FIND_ALL_QUERY = "SELECT id, name FROM rating";
    private static final String FIND_BY_ID_QUERY = FIND_ALL_QUERY + " WHERE id = ?";

    @Autowired
    public RatingStorage(JdbcTemplate jdbc, RowMapper<Rating> mapper, ResultSetExtractor<List<Rating>> extractor) {
        super(jdbc, mapper, extractor);
    }

    @Override
    public Collection<Rating> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Rating create(Rating user) {
        return null;
    }

    @Override
    public Rating update(Rating newUser) {
        return null;
    }

    @Override
    public Rating findById(Long id) {
        Optional<Rating> genreOpt = findOne(FIND_BY_ID_QUERY, id);
        if (genreOpt.isEmpty()) {
            log.error("Rating id: " + id + " doesn't exist");
            throw new NotFoundException("Рейтинг с id = " + id + " не найден");
        }

        return genreOpt.get();
    }
}
