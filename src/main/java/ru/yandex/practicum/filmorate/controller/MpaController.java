package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController()
@RequestMapping("/mpa")
@Slf4j
public class MpaController {
    private final MpaService mpaService;

    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }


    @GetMapping(value  = "/{id}")
    public Mpa get (@PathVariable("id") Integer mpa_id){
        return mpaService.getMpa(mpa_id);
    }

    @GetMapping
    public List<Mpa> getAll (){
        return mpaService.getAllMpa();
    }

}
