package com.example.project_web_cinema.service;

import com.example.project_web_cinema.repository.PayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PayRepository payRepository;

    @Transactional
    public void createPayment(Integer maDatVe, String paymentMethod, Double totalAmount) {
        String method = (paymentMethod != null && !paymentMethod.trim().isEmpty()) ? paymentMethod : "TienMat";
        payRepository.insertPayment(maDatVe, method, totalAmount, "ThanhCong");
    }
}