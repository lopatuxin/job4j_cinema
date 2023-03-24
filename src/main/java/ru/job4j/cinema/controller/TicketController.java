package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.service.FilmSessionService;
import ru.job4j.cinema.service.HallService;
import ru.job4j.cinema.service.TicketService;

import java.util.Optional;

@Controller
@RequestMapping("/tickets")
public class TicketController {
    private final TicketService ticketService;
    private final FilmSessionService filmSessionService;
    private final HallService hallService;

    public TicketController(TicketService ticketService, FilmSessionService filmSessionService, HallService hallService) {
        this.ticketService = ticketService;
        this.filmSessionService = filmSessionService;
        this.hallService = hallService;
    }

    @GetMapping("/{id}")
    public String getFilmSessionById(Model model, @PathVariable int id) {
        Optional<FilmSessionDto> filmSessionDtoOptional = filmSessionService.findById(id);
        if (filmSessionDtoOptional.isEmpty()) {
            model.addAttribute("message", "Попробуйте выбрать другой сеанс");
            return "errors/404";
        }
        String hallName = filmSessionDtoOptional.get().getHallsTitle();
        model.addAttribute("filmSession", filmSessionDtoOptional.get());
        model.addAttribute("rowNumbers", hallService.getRowsByName(hallName));
        model.addAttribute("placeNumbers", hallService.getPlacesByName(hallName));
        return "tickets/buy";
    }

    @PostMapping("/buy")
    public String buyTicket(@ModelAttribute Ticket ticket, Model model) {
        var ticketOptional = ticketService.save(ticket);
        if (ticketOptional.isEmpty()) {
            model.addAttribute("message",
                    """
                            Не удалось забронировать билет на выбранное место.\s
                            Возможно оно уже занято.\s
                            Попробуйте выбрать другое место.""");
            return "errors/404";
        }
        String messageSuccess = String.format("Вы забронировали билет на %s ряд %s место",
                ticketOptional.get().getRowNumber(),
                ticketOptional.get().getPlaceNumber());
        model.addAttribute("message", messageSuccess);
        return "tickets/successful_buy";
    }
}
