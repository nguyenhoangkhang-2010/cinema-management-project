package com.example.project_web_cinema.service;

import com.example.project_web_cinema.dto.user.AccountLevelDTO;
import com.example.project_web_cinema.entity.account.Account;
import com.example.project_web_cinema.repository.AccountRepository;
import com.example.project_web_cinema.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountLevelServiceImpl implements AccountLevelService {

    private final AccountRepository accountRepository;
    private final TicketRepository ticketRepository;

    @Override
    @Transactional
    public AccountLevelDTO getAccountLevel(String email) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));

        Integer totalTickets = ticketRepository.countPurchasedTicketsByAccountId(account.getMaTaiKhoan());
        if (totalTickets == null)
            totalTickets = 0;

        int level;
        int needed;
        int percentage;

        // Logic phân cấp độ
        if (totalTickets < 50) {
            level = 1;
            needed = 50 - totalTickets;
            percentage = (totalTickets * 100) / 50; // Progress của Cấp 1
        } else if (totalTickets < 150) {
            level = 2;
            needed = 150 - totalTickets;
            percentage = ((totalTickets - 50) * 100) / 100; // Progress của Cấp 2
        } else {
            level = 3;
            needed = 0;
            percentage = 100; // Max cấp độ
        }

        // Nếu cấp độ thay đổi, Update vào CSDL
        if (account.getCapDo() == null || account.getCapDo() != level) {
            account.setCapDo(level);
            accountRepository.save(account);
        }

        return AccountLevelDTO.builder()
                .maTaiKhoan(account.getMaTaiKhoan())
                .hoTen(account.getHoTen())
                .tongSoVeDaMua(totalTickets)
                .capDo(level)
                .soVeConThieuDeLenCap(needed)
                .phanTramTienTrinh(percentage)
                .build();
    }
}