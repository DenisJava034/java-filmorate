package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class MpaService {
    private final MpaDbStorage mpaDbStorage;


    public Collection<Mpa> getAll() {
        return mpaDbStorage.getAll();
    }


    public Mpa getById(final Long id) {
        return mpaDbStorage.getById(id).orElseThrow(() -> new NotFoundException("No mpa id = " + id));
    }


    public Mpa create(final Mpa data) {
        return mpaDbStorage.create(data).get();
    }


    public Mpa update(final Mpa data) {
        return mpaDbStorage.update(data).get();
    }


    public void delete() {
        mpaDbStorage.delete();
    }


    public void deleteById(final Long id) {
        mpaDbStorage.deleteById(id);
    }
}
