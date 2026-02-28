package com.crypto.watcher.crypto_watcher.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class NotificationService {

    @Async
    public void sendAlertNotification(String email, String symbol, BigDecimal price) {
        log.info("🔔 [ALARM GÖNDERİLDİ] -> Alıcı: {}, Coin: {}, Tetiklenen Fiyat: {}",
                email, symbol, price);
    }
}
