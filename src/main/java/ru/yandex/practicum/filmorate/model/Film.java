package ru.yandex.practicum.filmorate.model;

import ru.yandex.practicum.filmorate.utils.Marker;
import ru.yandex.practicum.filmorate.utils.DurationSerializer;
import ru.yandex.practicum.filmorate.utils.DurationDeserializer;
import ru.yandex.practicum.filmorate.utils.annotations.NullOrNotBlank;
import ru.yandex.practicum.filmorate.utils.annotations.PositiveDuration;
import ru.yandex.practicum.filmorate.utils.annotations.DateAfterBirthdayOfCinema;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;

/**
 * Film.
 */
@Data
public class Film {

    @NotNull(message = "Id должен быть указан", groups = Marker.OnUpdate.class)
    Long id;

    @NullOrNotBlank(message = "Название должено быть указано", groups = Marker.OnCreate.class)
    @NullOrNotBlank(message = "Название не может быть пустым", groups = Marker.OnUpdate.class, allowNull = true)
    String name;

    @Size(max = 200, message = "Максимальная длина описания — 200 символов")
    String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateAfterBirthdayOfCinema(message = "Дата релиза должна быть указана и быть позже 28.12.1895", groups = Marker.OnCreate.class)
    @DateAfterBirthdayOfCinema(message = "Дата релиза должна быть позже 28.12.1895", groups = Marker.OnUpdate.class, allowNull = true)
    LocalDate releaseDate;

    @JsonSerialize(using = DurationSerializer.class)
    @JsonDeserialize(using = DurationDeserializer.class)
    @PositiveDuration(message = "Продолжительность фильма должна быть указана и быть положительной", groups = Marker.OnCreate.class)
    @PositiveDuration(message = "Продолжительность фильма должна быть положительной", groups = Marker.OnUpdate.class, allowNull = true)
    Duration duration;
}