package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;
import ru.yandex.practicum.filmorate.utils.exception.NotFoundException;
import ru.yandex.practicum.filmorate.utils.exception.ConditionsNotMetException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Collection;

@Slf4j
@Service("filmServiceDb")
public class FilmServiceDb implements FilmService {

    private static final String SET_LIKE = "INSERT INTO user_like_film(film_id, user_id) VALUES (?, ?)";
    private static final String REMOVE_LIKE = "DELETE FROM user_like_film WHERE film_id = ? and user_id = ?";

    private final BaseDbStorage<Film> filmStorage;
    private final BaseDbStorage<User> userStorage;

    @Autowired
    public FilmServiceDb(BaseDbStorage<Film> filmStorage, BaseDbStorage<User> userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Override
    public Film findById(Long id) throws NotFoundException {
        return filmStorage.findById(id);
    }

    @Override
    public Film setLike(Long filmId, Long userId) throws NotFoundException {
        if (!filmStorage.isExists(filmId, "films")) {
            log.error("Film id: " + filmId + " doesn't exist");
            throw new NotFoundException("Film с id = " + filmId + " не найден");
        }
        if (!userStorage.isExists(userId, "users")) {
            log.error("User id: " + userId + " doesn't exist");
            throw new NotFoundException("User с id = " + userId + " не найден");
        }

        if (filmStorage.update(SET_LIKE, filmId, userId)) {
            log.info("User id=" + userId + " successfully liked the film id=" + filmId);
        }

        return filmStorage.findById(filmId);
    }

    @Override
    public Film deleteLike(Long filmId, Long userId) throws NotFoundException {
        if (!filmStorage.isExists(filmId, "films")) {
            log.error("Film id: " + filmId + " doesn't exist");
            throw new NotFoundException("Film с id = " + filmId + " не найден");
        }
        if (!userStorage.isExists(userId, "users")) {
            log.error("User id: " + userId + " doesn't exist");
            throw new NotFoundException("User с id = " + userId + " не найден");
        }

        if (filmStorage.delete(REMOVE_LIKE, filmId, userId)) {
            log.info("User id=" + userId + " successfully removed like from the film id=" + filmId);
        }

        return filmStorage.findById(filmId);
    }

    @Override
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

    @Override
    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    @Override
    public Film create(Film film) {
        return filmStorage.create(film);
    }

    @Override
    public Film update(Film newFilm) throws NotFoundException {
        return filmStorage.update(newFilm);
    }
}
