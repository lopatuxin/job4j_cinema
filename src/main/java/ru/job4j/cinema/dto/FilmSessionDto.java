package ru.job4j.cinema.dto;

import java.time.LocalDateTime;

public class FilmSessionDto {
    private int id;
    private String filmTitle;
    private String hallsTitle;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String price;

    public FilmSessionDto(int id, String filmTitle, String hallsTitle,
                          LocalDateTime startTime, LocalDateTime endTime, String price) {
        this.id = id;
        this.filmTitle = filmTitle;
        this.hallsTitle = hallsTitle;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilmTitle() {
        return filmTitle;
    }

    public void setFilmTitle(String filmTitle) {
        this.filmTitle = filmTitle;
    }

    public String getHallsTitle() {
        return hallsTitle;
    }

    public void setHallsTitle(String hallsTitle) {
        this.hallsTitle = hallsTitle;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
