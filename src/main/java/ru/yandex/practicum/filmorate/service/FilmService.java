package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.utils.exception.NotFoundException;
import ru.yandex.practicum.filmorate.utils.exception.ConditionsNotMetException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film setLike(Long filmId, Long userId) throws NotFoundException {
        Film film = filmStorage.findById(filmId);
        userStorage.findById(userId);

        film.getLikes().add(userId);

        log.info("User id=" + userId + " successfully liked the film id=" + filmId);

        return film;
    }

    public Film deleteLike(Long filmId, Long userId) throws NotFoundException {
        Film film = filmStorage.findById(filmId);
        userStorage.findById(userId);

        film.getLikes().remove(userId);

        log.info("User id=" + userId + " successfully removed like from the film id=" + filmId);

        return film;
    }

    public List<Film> getPopularFilms(int count) throws ConditionsNotMetException {
        if (count < 0) {
            log.error("Unable to get popular films. Count: " + count + "is negative");
            throw new ConditionsNotMetException("Значение count должно быть положительным. Count: " + count);
        }

        return filmStorage.findAll().stream()
                .sorted((f0, f1) -> f1.getLikes().size() - f0.getLikes().size())
                .limit(count)
                .toList();
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film newFilm) throws NotFoundException {
        return filmStorage.update(newFilm);
    }
}
