package ru.yandex.practicum.filmorate.service.genre;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.utils.exception.NotFoundException;

import java.util.Collection;

public interface GenreService {

    public Genre findById(Long id) throws NotFoundException;

    public Collection<Genre> findAll();
}
