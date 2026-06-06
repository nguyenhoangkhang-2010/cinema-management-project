package com.example.project_web_cinema.repository;

import com.example.project_web_cinema.entity.movietheater.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoomRepository extends JpaRepository<Room, Integer> {

    @Query("SELECT r FROM Room r LEFT JOIN r.cinema c WHERE (:search IS NULL OR r.tenPhong LIKE %:search% OR c.tenRap LIKE %:search%)")
    Page<Room> searchRooms(@Param("search") String search, Pageable pageable);
}