package com.crypto.watcher.crypto_watcher.entity;

import com.crypto.watcher.crypto_watcher.enums.AlertCondition;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "alert")
@Getter @Setter
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;
    private BigDecimal targetPrice;

    @Enumerated(EnumType.STRING)
    private AlertCondition condition; // ABOVE (Üstünde), BELOW (Altında)

    private boolean isTriggered = false; // Alarm çaldı mı?

    private String userEmail; // Kime haber verilecek?
}

