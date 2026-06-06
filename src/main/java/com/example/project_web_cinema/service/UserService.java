package com.example.project_web_cinema.service;

import com.example.project_web_cinema.dto.TicketDTO;
import com.example.project_web_cinema.dto.UserDTO;
import com.example.project_web_cinema.entity.account.Account;
import com.example.project_web_cinema.entity.account.VaiTro;
import com.example.project_web_cinema.entity.tickets.Tickets;
import com.example.project_web_cinema.repository.AccountRepository;
import com.example.project_web_cinema.repository.TicketRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    private final AccountRepository accountRepository;
    private final TicketRepository ticketRepository;

    public UserService(AccountRepository accountRepository, PasswordEncoder passwordEncoder, TicketRepository ticketRepository){
        this.accountRepository = accountRepository;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản: " + email));

        String vaiTro = account.getVaiTro() != null ? account.getVaiTro().toString() : "User";
        if (!vaiTro.startsWith("ROLE_")) {
            vaiTro = "ROLE_" + vaiTro;
        }

        return User.builder()
                .username(account.getHoTen())
                .password(account.getMatKhau())
                .authorities(vaiTro)
                .build();
    }

    public UserDTO getProfileByEmail(String email) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy email: " + email));

        List<Object[]> rawTickets = ticketRepository.findTicketHistoryByEmail(email);
        List<TicketDTO> listVe = new ArrayList<>();

        if (rawTickets != null) {
            for (Object[] row : rawTickets) {
                TicketDTO ve = TicketDTO.builder()
                        .id((Integer) row[0])
                        .tenPhim(String.valueOf(row[1]))
                        .tenPhong(String.valueOf(row[2]))
                        .tenRap(String.valueOf(row[3]))
                        .suatChieu(String.valueOf(row[4]))
                        .danhSachGhe(String.valueOf(row[5]))
                        .trangThai(String.valueOf(row[6]))
                        .build();
                listVe.add(ve);
            }
        }
        return UserDTO.builder()
                .hoTen(account.getHoTen())
                .ngaySinh(account.getNgaySinh())
                .email(account.getEmail())
                .soDienThoai(account.getSoDienThoai())
                .ngayTao(account.getNgayTao())
                .capDo(account.getCapDo() != null ? account.getCapDo() : 1)
                .vaiTro(account.getVaiTro() != null ? account.getVaiTro().toString() : "User")
                .loaiTaiKhoan(account.getLoaiTaiKhoan() != null ? account.getLoaiTaiKhoan().toString() : "Thuong")
                .trangThai(account.getTrangThai() != null ? account.getTrangThai().toString() : "HoatDong")
                .danhSachVe(listVe)
                .build();
    }
}
