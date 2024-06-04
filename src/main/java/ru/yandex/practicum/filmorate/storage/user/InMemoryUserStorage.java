package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.exception.NotFoundException;
import ru.yandex.practicum.filmorate.utils.exception.DuplicatedDataException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import java.util.Collection;
import java.util.function.Function;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User create(User user) throws DuplicatedDataException {
        String userLogin = user.getLogin();

        String userName = user.getName();
        if (userName == null || userName.isBlank()) {
            user.setName(userLogin);
        }

        checkDuplicate(user.getEmail(), User::getEmail);
        checkDuplicate(user.getLogin(), User::getLogin);

        user.setId(getNextId());
        users.put(user.getId(), user);

        log.info("User id=" + user.getId() + " successfully added");

        return user;
    }

    @Override
    public User update(User newUser) throws DuplicatedDataException, NotFoundException {
        User oldUser = users.get(newUser.getId());
        if (oldUser == null) {
            log.error("Unable to update user data. Id: " + newUser.getId() + " doesn't exist");
            throw new NotFoundException("User с id = " + newUser.getId() + " не найден");
        }

        Optional.ofNullable(newUser.getName()).ifPresent(oldUser::setName);
        Optional.ofNullable(newUser.getEmail()).ifPresent(email -> {
            checkDuplicate(email, User::getEmail);
            oldUser.setEmail(email);

        });
        Optional.ofNullable(newUser.getLogin()).ifPresent(login -> {
            checkDuplicate(login, User::getLogin);
            oldUser.setLogin(login);
        });
        Optional.ofNullable(newUser.getBirthday()).ifPresent(oldUser::setBirthday);

        log.info("User data id=" + newUser.getId() + " successfully updated");

        return oldUser;
    }

    @Override
    public User findById(Long id) throws NotFoundException {
        User u = users.get(id);
        if (u == null) {
            log.error("Unable to find user. Id: " + id + " doesn't exist");
            throw new NotFoundException("Пользователь с id=" + id + " не найден");
        }
        return u;
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void checkDuplicate(String value, Function<User, String> f) throws DuplicatedDataException {
        Optional<String> emailOpt = users.values().stream()
                .map(f)
                .filter(e -> e.equals(value))
                .findFirst();

        if (emailOpt.isPresent()) {
            log.error("Duplication. Value: " + value + " already exists");
            throw new DuplicatedDataException("Значение " + value + " уже используется");
        }
    }
}
