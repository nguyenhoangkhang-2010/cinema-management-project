package com.example.project_web_cinema.repository;

import com.example.project_web_cinema.entity.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByEmail(String email);
    Optional<Account> findBySoDienThoai(String soDienThoai);
    boolean existsByEmail(String email);
    boolean existsBySoDienThoai(String soDienThoai);
}
