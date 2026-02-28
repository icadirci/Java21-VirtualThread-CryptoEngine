package com.crypto.watcher.crypto_watcher.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class CryptoBackgroundService {
    private final CryptoService cryptoService;

    public CryptoBackgroundService(CryptoService cryptoService){
        this.cryptoService = cryptoService;
    }

    @Scheduled(fixedRate = 1000, initialDelay = 5000)
    public void fetchPricesPeriodically() {
        log.info(">>> Periyodik veri çekme işlemi başladı: {}", LocalDateTime.now());

        cryptoService.runLiveAnalysis();
    }
}
