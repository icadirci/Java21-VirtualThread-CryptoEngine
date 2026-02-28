package com.crypto.watcher.crypto_watcher.service;

import com.crypto.watcher.crypto_watcher.dto.BinancePriceRecord;
import com.crypto.watcher.crypto_watcher.entity.CryptoPrice;
import com.crypto.watcher.crypto_watcher.repository.PriceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class CryptoService {

    private final RestClient restClient;
    private final PriceRepository priceRepository;
    private final AlertService alertService;
    private final Map<String, BigDecimal> lastPrices = new ConcurrentHashMap<>();

    public CryptoService(RestClient rc, PriceRepository priceRepository, AlertService alertService){
        restClient = rc;
        this.priceRepository = priceRepository;
        this.alertService = alertService;
    }

    public void fetchRealPrice(String symbol) {
        try {
            BinancePriceRecord response = restClient.get()
                    .uri("https://api.binance.com/api/v3/ticker/price?symbol=" + symbol)
                    .retrieve()
                    .body(BinancePriceRecord.class);
            processPrice(symbol, response.price());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void runLiveAnalysis() {

        List<String> symbols = List.of(
                "BTCUSDT", "ETHUSDT", "BNBUSDT", "ADAUSDT", "SOLUSDT",
                "DOGEUSDT", "XRPUSDT", "DOTUSDT", "MATICUSDT", "LTCUSDT"
        );

        long startTime = System.currentTimeMillis();

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            symbols.forEach(symbol -> {
                executor.submit(() -> {
                    fetchRealPrice(symbol);
                });
            });
        }

        long endTime = System.currentTimeMillis();
        log.info("Canlı veri çekme tamamlandı. Toplam süre: {} ms", (endTime - startTime));
    }

    public void processPrice(String symbol, BigDecimal currentPrice) {
        BigDecimal lastPrice = lastPrices.get(symbol);
        if (lastPrice == null || !lastPrice.equals(currentPrice)){
            lastPrices.put(symbol, currentPrice);
            alertService.checkAlarms(symbol, currentPrice);
            saveToDatabase(symbol, currentPrice);
        }
    }

    private void saveToDatabase(String symbol, BigDecimal price) {
        CryptoPrice cryptoPrice = new CryptoPrice();
        cryptoPrice.setSymbol(symbol);
        cryptoPrice.setPrice(price);
        cryptoPrice.setTimestamp(LocalDateTime.now());

        priceRepository.save(cryptoPrice);
        // Virtual Thread burada DB cevabını beklerken carrier thread'i bırakır.
        // Sistem kilitlenmez!
    }
}
