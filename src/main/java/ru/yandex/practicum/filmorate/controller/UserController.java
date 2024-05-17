package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.utils.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.utils.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.Marker;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import java.util.Collection;
import java.util.function.Function;

@Validated
@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    @Validated({Marker.OnCreate.class})
    public User create(@Valid @RequestBody User user) {
        String userLogin = user.getLogin();

        String userName = user.getName();
        if (userName == null || userName.isBlank()) {
            user.setName(userLogin);
        }

        try {
            checkDuplicate(user.getEmail(), User::getEmail);
        } catch (DuplicatedDataException e) {
            log.error("Ошибка при добавления пользователя", e);
            throw new RuntimeException();
        }

        try {
            checkDuplicate(user.getLogin(), User::getLogin);
        } catch (DuplicatedDataException e) {
            log.error("Ошибка при добавления пользователя", e);
            throw new RuntimeException();
        }

        user.setId(getNextId());
        users.put(user.getId(), user);

        log.info("Пользователь успешно добавлен");

        return user;
    }

    @PutMapping
    @Validated({Marker.OnUpdate.class})
    public User update(@Valid @RequestBody User newUser) {
        User oldUser = users.get(newUser.getId());
        if (oldUser == null) {
            log.error("Не удалось обновить данные пользователя");
            throw new NotFoundException("User с id = " + newUser.getId() + " не найден");
        }

        Optional.ofNullable(newUser.getName()).ifPresent(oldUser::setName);
        Optional.ofNullable(newUser.getEmail()).ifPresent(email -> {
            try {
                checkDuplicate(email, User::getEmail);
                oldUser.setEmail(email);
            } catch (DuplicatedDataException e) {
                log.error("Ошибка при обновлении email", e);
                throw new RuntimeException();
            }
        });
        Optional.ofNullable(newUser.getLogin()).ifPresent(login -> {
            try {
                checkDuplicate(login, User::getLogin);
                oldUser.setLogin(login);
            } catch (DuplicatedDataException e) {
                log.error("Ошибка при обновлении login", e);
                throw new RuntimeException();
            }
        });
        Optional.ofNullable(newUser.getBirthday()).ifPresent(oldUser::setBirthday);

        log.info("Данные пользователя успешно обновлены");

        return oldUser;
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private boolean checkDuplicate(String value, Function<User, String> f) throws DuplicatedDataException {
        Optional<String> emailOpt = users.values().stream()
                .map(f)
                .filter(e -> e.equals(value))
                .findFirst();
        if (emailOpt.isPresent()) {
            throw new DuplicatedDataException("Значение " + value + " уже используется");
        }
        return true;
    }
}
