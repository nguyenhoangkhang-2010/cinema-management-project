package com.example.project_web_cinema.repository;

import com.example.project_web_cinema.entity.movietheater.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Integer> {
}
