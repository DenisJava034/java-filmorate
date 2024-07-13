package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class FilmRating {
    private Long id;
    private String name;
    private String description;
}