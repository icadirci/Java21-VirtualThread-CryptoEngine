package com.crypto.watcher.crypto_watcher.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "crypto_price")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CryptoPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;
    private BigDecimal price;
    private LocalDateTime timestamp;
}
