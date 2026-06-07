package com.example.project_web_cinema.dto.user;

import lombok.Data;
import java.util.List;

public class BookingDTOs {

    @Data
    public static class CinemaResDTO {
        private Integer id;
        private String name;
    }

    @Data
    public static class RoomResDTO {
        private Integer id;
        private String name;
    }

    @Data
    public static class SeatResDTO {
        private Integer maGhe;
        private String soGhe;
        private String loaiGhe;
        private Double giaPhuThu;
        private Boolean isBooked; // Đã thanh toán
        private Boolean isLocked; // Đang có người giữ (Hold 5 phút)
    }

    @Data
    public static class SelectSeatReqDTO {
        private Integer showtimeId;
        private List<Integer> seatIds;
    }

    @Data
    public static class ConfirmBookingReqDTO {
        private Integer showtimeId;
        private List<Integer> seatIds;
        private Double totalAmount;
    }
}