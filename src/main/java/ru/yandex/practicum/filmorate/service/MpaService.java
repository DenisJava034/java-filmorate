package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;

import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class MpaService implements IntefaceService<Mpa> {
    private final MpaDbStorage mpaDbStorage;

    @Override
    public Collection<Mpa> getAll() {
        return mpaDbStorage.getAll();
    }

    @Override
    public Mpa getById(final Long id) {
        return mpaDbStorage.getById(id).orElseThrow(() -> new NotFoundException("No mpa id = " + id));
    }

    @Override
    public Mpa create(final Mpa data) {
        return mpaDbStorage.create(data).get();
    }

    @Override
    public Mpa update(final Mpa data) {
        return mpaDbStorage.update(data).get();
    }

    @Override
    public void delete() {
        mpaDbStorage.delete();
    }

    @Override
    public void deleteById(final Long id) {
        mpaDbStorage.deleteById(id);
    }
}
