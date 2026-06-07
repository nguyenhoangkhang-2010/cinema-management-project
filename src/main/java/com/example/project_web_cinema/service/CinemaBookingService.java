package com.example.project_web_cinema.service;

import com.example.project_web_cinema.dto.user.BookingDTOs;
import java.util.List;

public interface CinemaBookingService {
    List<BookingDTOs.CinemaResDTO> getCinemasByMovie(Integer movieId);

    List<BookingDTOs.RoomResDTO> getRoomsByShowtime(Integer showtimeId);

    List<BookingDTOs.SeatResDTO> getSeats(Integer roomId, Integer showtimeId);

    boolean lockSeats(BookingDTOs.SelectSeatReqDTO req);

    void releaseSeats(BookingDTOs.SelectSeatReqDTO req);

    Integer confirmBooking(BookingDTOs.ConfirmBookingReqDTO req, String email);
}