package ru.yandex.practicum.filmorate.service.users;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;
import ru.yandex.practicum.filmorate.utils.exception.NotFoundException;
import ru.yandex.practicum.filmorate.utils.exception.DuplicatedDataException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.Collection;

@Slf4j
@Service("userServiceDb")
public class UserServiceDb implements UserService {
    private final BaseDbStorage<User> userStorage;

    private final static String FIND_ALL_FRIENDS = "SELECT u.*, f.user_id as friend_id " +
            "FROM friends f " +
            "LEFT JOIN users u on u.id = f.friend_id " +
            "WHERE f.user_id = ? ";
    private final static String FIND_COMMON_FRIENDS = "SELECT u.*, f3.friend_id " +
            "FROM friends f1 " +
            "JOIN friends f2 ON f1.friend_id = f2.friend_id " +
            "JOIN users u on u.id = f1.friend_id " +
            "LEFT JOIN friends f3 on f3.user_id = u.id " +
            "WHERE f1.user_id = ? and f2.user_id = ? and f1.approved = true";
    private final static String DELETE_FRIEND = "DELETE FROM friends WHERE user_id = ? and friend_id = ?";
    private final static String ADD_FRIEND = "INSERT INTO friends(user_id, friend_id, approved) VALUES (?, ?, true)";

    @Autowired
    public UserServiceDb(@Qualifier("userDbStorage") BaseDbStorage<User> userStorage) {
        this.userStorage = userStorage;
    }

    public User addFriend(Long id, Long friendId) throws NotFoundException {
        userStorage.findById(id);
        userStorage.findById(friendId);

        if (userStorage.update(ADD_FRIEND, id, friendId)) {
            log.info("User id=" + id + " successfully added friend user id=" + friendId);
        }
        return userStorage.findById(id);
    }


    public User deleteFriend(Long id, Long friendId) throws NotFoundException {
        userStorage.findById(id);
        userStorage.findById(friendId);

        if (userStorage.delete(DELETE_FRIEND, id, friendId)) {
            log.info("User id=" + id + " successfully removed friend user id=" + friendId);
        }

        return userStorage.findById(id);
    }


    public List<User> findAllFriends(Long id) throws NotFoundException {
        userStorage.findById(id);
        return userStorage.findMany(FIND_ALL_FRIENDS, id);
    }


    public List<User> findAllCommonFriends(Long id, Long otherId) throws NotFoundException {
        return userStorage.findMany(FIND_COMMON_FRIENDS, id, otherId);
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) throws DuplicatedDataException {
        return userStorage.create(user);
    }


    public User update(User newUser) throws DuplicatedDataException, NotFoundException {
        return userStorage.update(newUser);
    }
}
