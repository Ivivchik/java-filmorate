package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utils.exception.NotFoundException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import java.util.Collection;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private Map<Long, Film> films = new HashMap<>();

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Film create(Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);

        log.info("Film id=" + film.getId() + " successfully added");

        return film;
    }

    @Override
    public Film update(Film newFilm) throws NotFoundException {
        Film oldFilm = films.get(newFilm.getId());

        if (oldFilm == null) {
            log.error("Unable to update film data. Id: " + newFilm.getId() + " doesn't exist");
            throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
        }

        Optional.ofNullable(newFilm.getName()).ifPresent(oldFilm::setName);
        Optional.ofNullable(newFilm.getDescription()).ifPresent(oldFilm::setDescription);
        Optional.ofNullable(newFilm.getReleaseDate()).ifPresent(oldFilm::setReleaseDate);
        Optional.ofNullable(newFilm.getDuration()).ifPresent(oldFilm::setDuration);

        log.info("Film data id=" + newFilm.getId() + " successfully updated");

        return oldFilm;
    }

    @Override
    public Film findById(Long id) throws NotFoundException {
        Film film = films.get(id);

        if (film == null) {
            log.error("Unable to find film. Id: " + id + " doesn't exist");
            throw new NotFoundException("Фильм с id=" + id + " не найден");
        }
        return film;
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
