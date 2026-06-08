package com.example.project_web_cinema.service;

import com.example.project_web_cinema.dto.TicketDTO;
import com.example.project_web_cinema.dto.UserDTO;
import com.example.project_web_cinema.entity.account.Account;
import com.example.project_web_cinema.repository.AccountRepository;
import com.example.project_web_cinema.repository.TicketRepository;
import com.example.project_web_cinema.dto.BookingHistoryProjection;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    private final AccountRepository accountRepository;
    private final TicketRepository ticketRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public UserService(AccountRepository accountRepository, TicketRepository ticketRepository) {
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

        boolean isEnabled = account.getTrangThai() != null && account.getTrangThai().name().equals("HoatDong");

        return User.builder()
                .username(account.getEmail())
                .password(account.getMatKhau())
                .authorities(vaiTro)
                .disabled(!isEnabled)
                .build();
    }

    public UserDTO getProfileByEmail(String email) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy email: " + email));

        List<BookingHistoryProjection> rawTickets = ticketRepository.findTicketHistoryByEmail(email);
        List<TicketDTO> listVe = new ArrayList<>();

        if (rawTickets != null) {
            for (BookingHistoryProjection row : rawTickets) {
                TicketDTO ve = TicketDTO.builder()
                        .id(row.getMaDatVe())
                        .tenPhim(row.getTenPhim())
                        .tenPhong(row.getTenPhong())
                        .tenRap(row.getTenRap())
                        .suatChieu(row.getNgayChieu() + " " + row.getGioBatDau())
                        .danhSachGhe(row.getDanhSachGhe())
                        .trangThai(row.getTrangThai())
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

    @Transactional
    public void changeUserRole(String email, String role) {
        int updated = entityManager.createNativeQuery("UPDATE TAIKHOAN SET VaiTro = :role WHERE Email = :email")
                .setParameter("role", role)
                .setParameter("email", email)
                .executeUpdate();
        if (updated == 0) {
            throw new RuntimeException("Không tìm thấy tài khoản hoặc cập nhật thất bại!");
        }
    }
}