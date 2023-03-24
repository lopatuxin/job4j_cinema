package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.model.Genre;
import ru.job4j.cinema.repository.FilmRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SimpleFilmService implements FilmService {
    private final FilmRepository filmRepository;
    private final GenreService genreService;

    public SimpleFilmService(FilmRepository sql2oFilmRepository, GenreService genreService) {
        this.filmRepository = sql2oFilmRepository;
        this.genreService = genreService;
    }

    @Override
    public Optional<Film> findById(int id) {
        return filmRepository.findById(id);
    }

    @Override
    public Collection<FilmDto> findAll() {
        return filmRepository.findAll().stream()
                .map(film -> new FilmDto(film.getId(), film.getName(), film.getDescription(),
                        film.getYear(), genreNameById(film), film.getMinimalAge(),
                        film.getDurationInMinutes(), film.getFileId())).collect(Collectors.toList());
    }

    private String genreNameById(Film film) {
        String genreName = null;
        Optional<Genre> genreOptional = genreService.findById(film.getGenreId());
        if (genreOptional.isPresent()) {
            genreName = genreOptional.get().getName();
        }
        return genreName;
    }
}
