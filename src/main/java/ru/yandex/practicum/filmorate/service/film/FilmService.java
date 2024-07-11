package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utils.exception.NotFoundException;
import ru.yandex.practicum.filmorate.utils.exception.ConditionsNotMetException;

import java.util.Collection;
import java.util.List;

public interface FilmService {

    public Film findById(Long id) throws NotFoundException;

    public Film setLike(Long filmId, Long userId) throws NotFoundException;

    public Film deleteLike(Long filmId, Long userId) throws NotFoundException;

    public List<Film> getPopularFilms(int count) throws ConditionsNotMetException;

    public Collection<Film> findAll();

    public Film create(Film film);

    public Film update(Film newFilm) throws NotFoundException;
}
