package com.crypto.watcher.crypto_watcher.repository;

import com.crypto.watcher.crypto_watcher.entity.CryptoPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PriceRepository extends JpaRepository<CryptoPrice, Long> {
    Optional<CryptoPrice> findTopBySymbolOrderByIdDesc(String symbol);

}
