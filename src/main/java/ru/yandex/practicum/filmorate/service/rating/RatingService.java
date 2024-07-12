package ru.yandex.practicum.filmorate.service.rating;

import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.utils.exception.NotFoundException;

import java.util.Collection;

public interface RatingService {
    public Rating findById(Long id) throws NotFoundException;

    public Collection<Rating> findAll();
}
