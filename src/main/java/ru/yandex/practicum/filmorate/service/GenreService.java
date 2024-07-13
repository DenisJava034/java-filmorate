package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreService {
    private final GenreDbStorage genreDbStorage;


    public Collection<Genre> getAll() {
        return genreDbStorage.getAll();
    }


    public Genre getById(final Long id) {
        return genreDbStorage.getById(id).orElseThrow(() -> new NotFoundException("No genre id = " + id));
    }


    public Genre create(final Genre data) {
        return genreDbStorage.create(data).get();
    }


    public Genre update(final Genre data) {
        return genreDbStorage.update(data).get();
    }


    public void delete() {
        genreDbStorage.delete();
    }


    public void deleteById(final Long id) {
        genreDbStorage.deleteById(id);
    }
}
