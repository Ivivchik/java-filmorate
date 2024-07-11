package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utils.Marker;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Collection;

@Validated
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(@Qualifier("filmServiceDb") FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> findAll() {
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public Film getById(@PathVariable Long id) {
        return filmService.findById(id);
    }

    @PostMapping
    @Validated({Marker.OnCreate.class})
    public Film create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping
    @Validated({Marker.OnUpdate.class})
    public Film update(@Valid @RequestBody Film newFilm) {
        return filmService.update(newFilm);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film setLike(@PathVariable Long id, @PathVariable Long userId) {
        return filmService.setLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        return filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        return filmService.getPopularFilms(count);
    }
}