package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;
import ru.yandex.practicum.filmorate.utils.exception.NotFoundException;
import ru.yandex.practicum.filmorate.utils.exception.ConditionsNotMetException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service("filmServiceInMemory")
public class FilmServiceInMemory implements FilmService {
    private final Storage<Film> filmStorage;
    private final Storage<User> userStorage;

    @Autowired
    public FilmServiceInMemory(@Qualifier("inMemoryFilmStorage") Storage<Film> filmStorage,
                               @Qualifier("inMemoryUserStorage") Storage<User> userStorage) {

        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Override
    public Film findById(Long id) throws NotFoundException {
        return filmStorage.findById(id);
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
