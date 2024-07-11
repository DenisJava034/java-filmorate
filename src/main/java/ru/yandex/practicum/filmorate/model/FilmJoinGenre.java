package ru.yandex.practicum.filmorate.model;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FilmJoinGenre {
    private long filmId;
    private Long id;
    private String name;
}