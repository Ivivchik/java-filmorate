package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.rating.RatingService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
public class RatingController {
    private final RatingService ratingService;

    @Autowired
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @GetMapping
    public Collection<Rating> findAll() {
        return ratingService.findAll();
    }

    @GetMapping("/{id}")
    public Rating getById(@PathVariable Long id) {
        return ratingService.findById(id);
    }
}
