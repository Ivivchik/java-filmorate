package ru.yandex.practicum.filmorate.storage.user.mapper;

import ru.yandex.practicum.filmorate.model.User;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();

        user.setId(rs.getLong("id"));
        user.setName(rs.getString("name"));
        user.setLogin(rs.getString("login"));
        user.setEmail(rs.getString("email"));
        user.setBirthday(rs.getObject("birthday", LocalDate.class));

        do {
            user.getFriends().add(rs.getLong("friend_id"));
        } while (rs.next());

        return user;
    }
}
