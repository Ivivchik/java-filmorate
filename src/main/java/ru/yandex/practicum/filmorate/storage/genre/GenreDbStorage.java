package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;
import ru.yandex.practicum.filmorate.utils.exception.NotFoundException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository("genreDbStorage")
public class GenreDbStorage extends BaseDbStorage<Genre> {
    private static final String FIND_ALL_QUERY = "SELECT id, name FROM genre";
    private static final String FIND_BY_ID_QUERY = FIND_ALL_QUERY + " WHERE id = ?";

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper, ResultSetExtractor<List<Genre>> extractor) {
        super(jdbc, mapper, extractor);
    }

    @Override
    public Collection<Genre> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Genre create(Genre user) {
        return null;
    }

    @Override
    public Genre update(Genre newUser) {
        return null;
    }

    @Override
    public Genre findById(Long id) {
        Optional<Genre> genreOpt = findOne(FIND_BY_ID_QUERY, id);
        if (genreOpt.isEmpty()) {
            log.error("Genre id: " + id + " doesn't exist");
            throw new NotFoundException("Жанр с id = " + id + " не найден");
        }

        return genreOpt.get();
    }
}
