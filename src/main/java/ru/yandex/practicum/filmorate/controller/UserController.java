package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.Marker;
import ru.yandex.practicum.filmorate.service.users.UserService;

import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Collection;

@Validated
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(@Qualifier("userServiceDb") UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> findAll() {
        return userService.findAll();
    }

    @PostMapping
    @Validated({Marker.OnCreate.class})
    public User create(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping
    @Validated({Marker.OnUpdate.class})
    public User update(@Valid @RequestBody User newUser) {
        return userService.update(newUser);
    }

    @PutMapping("/{id}/friends/{friendsId}")
    public User addFriend(@PathVariable Long id, @PathVariable Long friendsId) {
        return userService.addFriend(id, friendsId);
    }

    @DeleteMapping("/{id}/friends/{friendsId}")
    public User deleteFriend(@PathVariable Long id, @PathVariable Long friendsId) {
        return userService.deleteFriend(id, friendsId);
    }

    @GetMapping("/{id}/friends")
    public List<User> findAllFriends(@PathVariable Long id) {
        return userService.findAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> findAllCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return userService.findAllCommonFriends(id, otherId);
    }

}
