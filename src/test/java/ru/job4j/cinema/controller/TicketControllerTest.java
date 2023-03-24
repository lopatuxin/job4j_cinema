package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.service.FilmSessionService;
import ru.job4j.cinema.service.HallService;
import ru.job4j.cinema.service.TicketService;

import java.util.Optional;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TicketControllerTest {
    private TicketService ticketService;
    private FilmSessionService filmSessionService;
    private HallService hallService;
    private TicketController ticketController;

    @BeforeEach
    public void initServices() {
        ticketService = mock(TicketService.class);
        filmSessionService = mock(FilmSessionService.class);
        hallService = mock(HallService.class);
        ticketController = new TicketController(ticketService, filmSessionService, hallService);
    }

    @Test
    public void whenFoundFilmSessionByIdThenGetTicketsBuy() {
        var filmSession = new FilmSessionDto(1, "film", "hall", now().plusHours(1),
                now().plusHours(3), "300");
        when(filmSessionService.findById(filmSession.getId())).thenReturn(Optional.of(filmSession));

        var model = new ConcurrentModel();
        var view = ticketController.getFilmSessionById(model, filmSession.getId());
        var actualFilmSession = model.getAttribute("filmSession");

        assertThat(view).isEqualTo("tickets/buy");
        assertThat(actualFilmSession).isEqualTo(filmSession);
    }

    @Test
    public void whenNotFoundFilmSessionByIdThenGetErrorPage() {
        int invalidId = 1;
        when(filmSessionService.findById(1)).thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var view = ticketController.getFilmSessionById(model, invalidId);
        var actualMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualMessage).isEqualTo("Попробуйте выбрать другой сеанс");
    }

    @Test
    public void whenRequestBuyTicketThenGetSuccessfulBuyPage() {
        var ticket = new Ticket(1, 1, 1, 1, 1);
        when(ticketService.save(ticket)).thenReturn(Optional.of(ticket));

        var model = new ConcurrentModel();
        var view = ticketController.buyTicket(ticket, model);
        var actualMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("tickets/successful_buy");
        assertThat(actualMessage).isEqualTo("Вы забронировали билет на 1 ряд 1 место");
    }

    @Test
    public void whenRequestBuyTicketThenGetErrorPage() {
        String expectedMessage = """
                            Не удалось забронировать билет на выбранное место.\s
                            Возможно оно уже занято.\s
                            Попробуйте выбрать другое место.""";
        var ticket = new Ticket(1, 1, 1, 1, 1);
        when(ticketService.save(ticket)).thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var view = ticketController.buyTicket(ticket, model);
        var actualMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualMessage).isEqualTo(expectedMessage);
    }
}