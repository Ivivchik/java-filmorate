package ru.yandex.practicum.filmorate.storage.user;


import lombok.extern.slf4j.Slf4j;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;
import ru.yandex.practicum.filmorate.utils.exception.NotFoundException;
import ru.yandex.practicum.filmorate.utils.exception.DuplicatedDataException;

import java.util.*;

@Slf4j
@Repository("userDbStorage")
public class UserDbStorage extends BaseDbStorage<User> {

    private static final String FIND_ALL_QUERY = "SELECT u.*, f.friend_id " +
            "FROM users u " +
            "LEFT JOIN friends f ON u.id = f.user_id";
    private static final String FIND_BY_ID_QUERY = "SELECT u.*, f.friend_id " +
            "FROM users u " +
            "LEFT JOIN friends f ON u.id = f.user_id " +
            "WHERE u.id = ? ";

    @Autowired
    protected UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper, ResultSetExtractor<List<User>> extractor) {
        super(jdbc, mapper, extractor);
    }

    @Override
    public Collection<User> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public User create(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbc)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> m = new HashMap<>();
        m.put("name", user.getName());
        m.put("login", checkDuplicate("login", user.getLogin()));
        m.put("email", checkDuplicate("email", user.getEmail()));
        m.put("birthday", user.getBirthday());

        Long id = simpleJdbcInsert.executeAndReturnKey(m).longValue();
        user.setId(id);

        log.info("User id=" + user.getId() + " successfully added");

        return user;
    }

    @Override
    public User update(User newUser) throws NotFoundException, DuplicatedDataException {
        Long userId = newUser.getId();
        if (!isExists(userId)) {
            log.error("Unable to update user data. Id: " + userId + " doesn't exist");
            throw new NotFoundException("User с id = " + userId + " не найден");
        }

        StringBuilder query = new StringBuilder("UPDATE users SET ");
        List<Object> parameters = new ArrayList<>();

        Optional.ofNullable(newUser.getName()).ifPresent(name -> {
            parameters.add(name);
            query.append("name = ?, ");
        });
        Optional.ofNullable(newUser.getBirthday()).ifPresent(birthday -> {
            parameters.add(birthday);
            query.append("birthday = ?, ");
        });
        Optional.ofNullable(newUser.getEmail()).ifPresent(email -> {
            parameters.add(checkDuplicate("email", email));
            query.append("email = ?, ");
        });
        Optional.ofNullable(newUser.getLogin()).ifPresent(login -> {
            parameters.add(checkDuplicate("login", login));
            query.append("login = ?, ");
        });

        query.delete(query.length() - 2, query.length());
        query.append(" WHERE id = ?");
        parameters.add(userId);

        jdbc.update(query.toString(), parameters.toArray());

        log.info("User data id=" + newUser.getId() + " successfully updated");

        return findById(userId);

    }

    @Override
    public User findById(Long id) {
        Optional<User> userOpt = findOne(FIND_BY_ID_QUERY, id);
        if (userOpt.isEmpty()) {
            log.error("User id: " + id + " doesn't exist");
            throw new NotFoundException("User с id = " + id + " не найден");
        }

        return userOpt.get();
    }

    private <T> T checkDuplicate(String filedName, T value) throws DuplicatedDataException {
        String query = "SELECT COUNT(1) FROM users WHERE " + filedName + " = ?";
        int count = jdbc.queryForObject(query, Integer.class, value);

        if (count > 0) {
            log.error("Duplication. Value: " + value + " already exists");
            throw new DuplicatedDataException("Этот " + filedName + " уже используется");
        }

        return value;
    }

    private boolean isExists(Long id) {
        String query = "SELECT COUNT(1) FROM users WHERE id = ?";
        int count = jdbc.queryForObject(query, Integer.class, id);

        return count > 0;
    }
}
