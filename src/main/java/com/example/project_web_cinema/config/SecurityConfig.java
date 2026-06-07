package com.example.project_web_cinema.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. TẮT CSRF: Giúp form POST dữ liệu an toàn khi chạy local, không bị chặn bộ nhớ Session Cookie
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/register", "/login", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("Admin")
                        .anyRequest().authenticated())

                .formLogin(login -> login
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler((request, response, authentication) -> {
                            var authorities = authentication.getAuthorities();
                            boolean isAdmin = authorities.stream()
                                    .anyMatch(a -> a.getAuthority().equals("ROLE_Admin"));

                            if (isAdmin) {
                                response.sendRedirect("/admin/movies");
                            } else {
                                response.sendRedirect("/");
                            }
                        })
                        .permitAll())

                // 2. CẤU HÌNH SESSION: Ép hệ thống luôn duy trì và bám sát Session Id lưu trên trình duyệt người dùng
                .sessionManagement(session -> session
                        .sessionFixation(sessionFixation -> sessionFixation.newSession())
                        .maximumSessions(1)
                )

                // 3. BẬT TÍNH NĂNG GHI NHỚ TÀI KHOẢN (Đã đồng bộ với Checkbox name="remember-me" trong file login.html của bạn)
                .rememberMe(remember -> remember
                        .rememberMeParameter("remember-me")
                        .tokenValiditySeconds(86400 * 7)
                        .key("allstar_cinema_secret_security_key")
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID", "remember-me")
                        .permitAll());

        return http.build();
    }
}