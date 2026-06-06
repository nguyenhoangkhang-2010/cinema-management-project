package com.example.project_web_cinema.repository;

import com.example.project_web_cinema.entity.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<Account, Integer> {
    @Query(value = "SELECT COUNT(id) FROM user WHERE DATE(ngayTao) >= DATE_SUB(NOW(), INTERVAL 7 DAY) GROUP BY DATE(ngayTao) ORDER BY ngayTao DESC LIMIT 7", nativeQuery = true)
    List<Long> getUserCountLast7Days();
}
