package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.utils.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.utils.exception.NotFoundException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addFriend(Long id, Long friendId) throws NotFoundException {
        User user = userStorage.findById(id);
        User friend = userStorage.findById(friendId);

        user.getFriends().add(friendId);
        friend.getFriends().add(id);

        log.info("User id=" + id + " successfully added friend user id=" + friendId);

        return user;
    }

    public User deleteFriend(Long id, Long friendId) throws NotFoundException {
        User user = userStorage.findById(id);
        User friend = userStorage.findById(friendId);

        user.getFriends().remove(friendId);
        friend.getFriends().remove(id);

        log.info("User id=" + id + " successfully removed friend user id=" + friendId);

        return user;
    }

    public List<User> findAllFriends(Long id) throws NotFoundException {
        User user = userStorage.findById(id);

        return user.getFriends().stream()
                .map(userStorage::findById)
                .toList();
    }

    public List<User> findAllCommonFriends(Long id, Long otherId) throws NotFoundException {
        User user = userStorage.findById(id);
        User otherUser = userStorage.findById(otherId);

        Set<Long> otherUserFriend = otherUser.getFriends();

        return user.getFriends().stream()
                .filter(otherUserFriend::contains)
                .map(userStorage::findById)
                .toList();
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
