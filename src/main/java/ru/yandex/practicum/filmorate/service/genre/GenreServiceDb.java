package ru.yandex.practicum.filmorate.service.genre;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;
import ru.yandex.practicum.filmorate.utils.exception.NotFoundException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

@Slf4j
@Service("genreServiceDb")
public class GenreServiceDb implements GenreService {
    private final BaseDbStorage<Genre> genreStorage;

    @Autowired
    public GenreServiceDb(BaseDbStorage<Genre> genreStorage) {
        this.genreStorage = genreStorage;
    }

    @Override
    public Genre findById(Long id) throws NotFoundException {
        return genreStorage.findById(id);
    }

    @Override
    public Collection<Genre> findAll() {
        return genreStorage.findAll();
    }
}
