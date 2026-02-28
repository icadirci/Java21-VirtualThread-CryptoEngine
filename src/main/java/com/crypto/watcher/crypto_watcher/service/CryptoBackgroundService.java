package com.crypto.watcher.crypto_watcher.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CryptoBackgroundService {
    private final CryptoService cryptoService;

    public CryptoBackgroundService(CryptoService cryptoService){
        this.cryptoService = cryptoService;
    }

    /**
     * Triggers periodic price fetching via scheduler.
     */
    @Scheduled(fixedRate = 1000, initialDelay = 5000)
    public void fetchPricesPeriodically() {
        log.info(">>> Periyodik veri çekme işlemi başladı: {}");

        cryptoService.runLiveAnalysis();
    }
}
