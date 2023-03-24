package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.model.FilmSession;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.repository.FilmSessionRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SimpleFilmSessionService implements FilmSessionService {
    private final FilmSessionRepository filmSessionRepository;
    private final FilmService filmService;
    private final HallService hallService;

    public SimpleFilmSessionService(FilmSessionRepository sql2oFilmSessionRepository,
                                    FilmService filmService, HallService hallService) {
        this.filmSessionRepository = sql2oFilmSessionRepository;
        this.filmService = filmService;
        this.hallService = hallService;
    }

    @Override
    public Optional<FilmSessionDto> findById(int id) {
        Optional<FilmSession> filmSessionOptional = filmSessionRepository.findById(id);
        if (filmSessionOptional.isPresent()) {
            var filmSessionDto = new FilmSessionDto(
                    filmSessionOptional.get().getId(),
                    getFilmTitle(filmSessionOptional.get()),
                    getHallTitle(filmSessionOptional.get()),
                    filmSessionOptional.get().getStartTime(),
                    filmSessionOptional.get().getEndTime(),
                    String.valueOf(filmSessionOptional.get().getPrice()));
            return Optional.of(filmSessionDto);
        }
        return Optional.empty();
    }

    @Override
    public Collection<FilmSessionDto> findAll() {
        return filmSessionRepository.findAll().stream()
                .map(filmSession -> new FilmSessionDto(filmSession.getId(), getFilmTitle(filmSession),
                        getHallTitle(filmSession), filmSession.getStartTime(), filmSession.getEndTime(),
                        String.valueOf(filmSession.getPrice()))).collect(Collectors.toList());
    }

    private String getFilmTitle(FilmSession filmSession) {
        String filmTitle = null;
        Optional<Film> filmOptional = filmService.findById(filmSession.getFilmId());
        if (filmOptional.isPresent()) {
            filmTitle = filmOptional.get().getName();
        }
        return filmTitle;
    }

    private String getHallTitle(FilmSession filmSession) {
        String hallTitle = null;
        Optional<Hall> hallOptional = hallService.findById(filmSession.getHallsId());
        if (hallOptional.isPresent()) {
            hallTitle = hallOptional.get().getName();
        }
        return hallTitle;
    }
}
