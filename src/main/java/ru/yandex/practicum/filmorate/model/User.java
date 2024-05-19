package ru.yandex.practicum.filmorate.model;

import ru.yandex.practicum.filmorate.utils.Marker;
import ru.yandex.practicum.filmorate.utils.annotations.CustomEmail;
import ru.yandex.practicum.filmorate.utils.annotations.NullOrNotBlank;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

import java.time.LocalDate;

@Data
public class User {

    @NotNull(message = "Id должен быть указан", groups = Marker.OnUpdate.class)
    Long id;

    @CustomEmail(message = "Электронная почта не может быть пустой и содержать @", groups = Marker.OnCreate.class)
    @CustomEmail(message = "Электронная почта должна содержать @", groups = Marker.OnUpdate.class, allowNull = true)
    String email;

    @NullOrNotBlank(message = "Логин должен быть указан", groups = Marker.OnCreate.class)
    @NullOrNotBlank(message = "Логин не может быть пустым", groups = Marker.OnUpdate.class, allowNull = true)
    String login;

    String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Past(message = "Дата рождения не может быть в будущем")
    LocalDate birthday;
}
