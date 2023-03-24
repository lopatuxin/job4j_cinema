package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.repository.HallRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class SimpleHallService implements HallService {
    private final HallRepository hallRepository;

    public SimpleHallService(HallRepository sql2oHallRepository) {
        this.hallRepository = sql2oHallRepository;
    }

    @Override
    public Optional<Hall> findById(int id) {
        return hallRepository.findById(id);
    }

    @Override
    public Optional<Hall> findByName(String name) {
        return hallRepository.findByName(name);
    }

    @Override
    public Collection<Hall> findAll() {
        return hallRepository.findAll();
    }

    @Override
    public List<Integer> getRowsByName(String name) {
        List<Integer> rows = new ArrayList<>();
        for (int i = 1; i <= hallRepository.findByName(name).get().getRowCount(); i++) {
            rows.add(i);
        }
        return rows;
    }

    @Override
    public List<Integer> getPlacesByName(String name) {
        List<Integer> places = new ArrayList<>();
        for (int i = 1; i <= hallRepository.findByName(name).get().getPlaceCount(); i++) {
            places.add(i);
        }
        return places;
    }
}
