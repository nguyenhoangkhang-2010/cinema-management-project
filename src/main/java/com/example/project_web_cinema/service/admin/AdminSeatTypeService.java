package com.example.project_web_cinema.service.admin;

import com.example.project_web_cinema.dto.admin.SeatTypeDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminSeatTypeService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<SeatTypeDTO> getAllSeatTypes() {
        List<Object[]> rows = entityManager
                .createNativeQuery("SELECT MaLoaiGhe, TenLoaiGhe, GiaPhuThu FROM LOAIGHE ORDER BY GiaPhuThu ASC")
                .getResultList();
        return rows.stream().map(r -> SeatTypeDTO.builder()
                .maLoaiGhe((Integer) r[0])
                .tenLoaiGhe((String) r[1])
                .giaPhuThu(((Number) r[2]).doubleValue())
                .build()).collect(Collectors.toList());
    }

    @Transactional
    public void saveSeatType(SeatTypeDTO dto) {
        if (dto.getMaLoaiGhe() != null) {
            entityManager.createNativeQuery("UPDATE LOAIGHE SET TenLoaiGhe = ?, GiaPhuThu = ? WHERE MaLoaiGhe = ?")
                    .setParameter(1, dto.getTenLoaiGhe())
                    .setParameter(2, dto.getGiaPhuThu())
                    .setParameter(3, dto.getMaLoaiGhe())
                    .executeUpdate();
        } else {
            entityManager.createNativeQuery("INSERT INTO LOAIGHE (TenLoaiGhe, GiaPhuThu) VALUES (?, ?)")
                    .setParameter(1, dto.getTenLoaiGhe())
                    .setParameter(2, dto.getGiaPhuThu())
                    .executeUpdate();
        }
    }

    @Transactional
    public void deleteSeatType(Integer id) {
        entityManager.createNativeQuery("DELETE FROM LOAIGHE WHERE MaLoaiGhe = ?").setParameter(1, id).executeUpdate();
    }
}
