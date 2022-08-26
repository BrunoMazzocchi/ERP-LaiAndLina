package com.laiandlina.erp.persistance.entity;

import lombok.*;

import javax.persistence.*;
import java.time.*;

@Data
@Entity
public class RefreshToken {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "refresh_token_seq")
    @SequenceGenerator(name = "refresh_token_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "TOKEN", nullable = false, unique = true)
    private String token;

    @Column(name = "REFRESH_COUNT")
    private Integer refreshCount;

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_DEVICE_ID", unique = true)
    private UserDevice userDevice;

    @Column(name = "EXPIRY_DT", nullable = false)
    private Instant expiryDate;

    public void incrementRefreshCount() {
        refreshCount = refreshCount + 1;
    }
}