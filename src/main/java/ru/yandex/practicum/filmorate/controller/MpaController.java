package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
@AllArgsConstructor
public class MpaController {
    private final MpaService mpaService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<MpaRating> getAllMpaRatings() {
        return mpaService.getAllMpaRatings();
    }

    @GetMapping("/{mpa_id}")
    @ResponseStatus(HttpStatus.OK)
    public MpaRating getMpaRating(@PathVariable int mpa_id) {
        return mpaService.getMpaRating(mpa_id);
    }

}
