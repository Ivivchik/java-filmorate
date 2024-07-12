package ru.yandex.practicum.filmorate.service.rating;

import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;
import ru.yandex.practicum.filmorate.utils.exception.NotFoundException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

@Slf4j
@Service("ratingServiceDb")
public class RatingServiceDb implements RatingService {

    private final BaseDbStorage<Rating> ratingStorage;

    @Autowired
    public RatingServiceDb(BaseDbStorage<Rating> ratingStorage) {
        this.ratingStorage = ratingStorage;
    }

    @Override
    public Rating findById(Long id) throws NotFoundException {
        return ratingStorage.findById(id);
    }

    @Override
    public Collection<Rating> findAll() {
        return ratingStorage.findAll();
    }
}
