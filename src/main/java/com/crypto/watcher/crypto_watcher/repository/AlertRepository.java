package com.crypto.watcher.crypto_watcher.repository;

import com.crypto.watcher.crypto_watcher.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    /**
     * Returns non-triggered (active) alerts for the given symbol.
     */
    List<Alert> findBySymbolAndIsTriggeredFalse(String symbol);

    /**
     * Returns all non-triggered (active) alerts.
     */
    List<Alert> findByIsTriggeredFalse();
}
