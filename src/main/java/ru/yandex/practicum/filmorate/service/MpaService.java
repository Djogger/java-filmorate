package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class MpaService {
    private final MpaDbStorage inMemoryMpaStorage;

    public Collection<MpaRating> getAllMpaRatings() {
        return inMemoryMpaStorage.getAllMpaRatings();
    }

    public MpaRating getMpaRating(int mpa_id) {
        Optional<MpaRating> mpaRating = inMemoryMpaStorage.getMpaRating(mpa_id);

        if (mpaRating.isEmpty()) {
            throw new NotFoundException("MPA рейтинга с id = " + mpa_id + " не найдено");
        }

        return mpaRating.get();
    }

}
