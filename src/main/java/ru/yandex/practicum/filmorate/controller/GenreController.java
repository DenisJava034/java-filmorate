package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;


@RestController
@RequestMapping("/genres")
@Slf4j
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Genre> getAll() {
        return genreService.getAll();
    }

    @GetMapping("/{genre_id}")
    @ResponseStatus(HttpStatus.OK)
    public Genre getById(@PathVariable("genre_id") final Long id) {
        return genreService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Genre create(@Valid @RequestBody final Genre data) {
        return genreService.create(data);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Genre update(@Valid @RequestBody final Genre data) {
        return genreService.update(data);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void delete() {
        genreService.delete();
    }

    @DeleteMapping("/{genre_id}")
    public void deleteById(@Valid @PathVariable("genre_id") Long id) {
        genreService.deleteById(id);
    }
}