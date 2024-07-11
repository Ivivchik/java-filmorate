package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;
import java.util.Optional;

public abstract class BaseDbStorage<T> implements Storage<T> {

    protected final JdbcTemplate jdbc;
    protected final RowMapper<T> mapper;
    protected final ResultSetExtractor<List<T>> extractor;

    protected BaseDbStorage(JdbcTemplate jdbc, RowMapper<T> mapper, ResultSetExtractor<List<T>> extractor) {
        this.jdbc = jdbc;
        this.mapper = mapper;
        this.extractor = extractor;
    }

    public Optional<T> findOne(String query, Object... params) {
        try {
            T result = jdbc.queryForObject(query, mapper, params);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    public List<T> findMany(String query, Object... params) {
        return jdbc.query(query, extractor, params);
    }

    public boolean delete(String query, Object... params) {
        int rowDeleted = jdbc.update(query, params);
        return rowDeleted > 0;
    }

    public boolean update(String query, Object... params) {
        int rowUpdated = jdbc.update(query, params);
        return rowUpdated > 0;
    }

    public boolean isExists(Long id, String tableName) {
        String query = "SELECT COUNT(1) FROM " + tableName + " WHERE id = ?";
        int count = jdbc.queryForObject(query, Integer.class, id);

        return count > 0;
    }
}
