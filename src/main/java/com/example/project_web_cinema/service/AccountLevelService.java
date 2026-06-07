package com.example.project_web_cinema.service;

import com.example.project_web_cinema.dto.user.AccountLevelDTO;

public interface AccountLevelService {
    AccountLevelDTO getAccountLevel(String email);
}