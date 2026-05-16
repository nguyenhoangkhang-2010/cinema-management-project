package com.example.project_web_cinema.service;

import com.example.project_web_cinema.entity.account.Account;
import com.example.project_web_cinema.entity.account.VaiTro;
import com.example.project_web_cinema.repository.AccountRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(AccountRepository accountRepository, PasswordEncoder passwordEncoder){
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản với email: " + email));

        String vaiTro = account.getVaiTro().toString();
        if (!vaiTro.startsWith("ROLE_")) {
            vaiTro = "ROLE_" + vaiTro;
        }

        return User.builder()
                .username(account.getEmail())
                .password(account.getMatKhau())
                .authorities(vaiTro)
                .build();
    }

    public void registerNewAccount(Account account) {
        String hashedPassword = passwordEncoder.encode(account.getMatKhau());
        account.setMatKhau(hashedPassword);

        account.setVaiTro(VaiTro.User);
        accountRepository.save(account);
    }
}
