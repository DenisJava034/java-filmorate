package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;

import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreService implements IntefaceService<Genre> {
    private final GenreDbStorage genreDbStorage;

    @Override
    public Collection<Genre> getAll() {
        return genreDbStorage.getAll();
    }

    @Override
    public Genre getById(final Long id) {
        return genreDbStorage.getById(id).orElseThrow(() -> new NotFoundException("No genre id = " + id));
    }

    @Override
    public Genre create(final Genre data) {
        return genreDbStorage.create(data).get();
    }

    @Override
    public Genre update(final Genre data) {
        return genreDbStorage.update(data).get();
    }

    @Override
    public void delete() {
        genreDbStorage.delete();
    }

    @Override
    public void deleteById(final Long id) {
        genreDbStorage.deleteById(id);
    }
}
