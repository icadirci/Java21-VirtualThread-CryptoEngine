package com.crypto.watcher.crypto_watcher.service;

import com.crypto.watcher.crypto_watcher.dto.request.CreateAlertRequest;
import com.crypto.watcher.crypto_watcher.entity.Alert;
import com.crypto.watcher.crypto_watcher.enums.AlertCondition;
import com.crypto.watcher.crypto_watcher.repository.AlertRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AlertService {
    private final AlertRepository alertRepository;
    private final NotificationService notificationService;

    public AlertService(AlertRepository alertRepository, NotificationService notificationService) {
        this.alertRepository = alertRepository;
        this.notificationService = notificationService;
    }

    /**
     * Returns all non-triggered alert records.
     */
    public List<Alert> allAlerts(){
        return alertRepository.findByIsTriggeredFalse();
    }

    /**
     * Creates a new alert record.
     *
     * @param createAlertRequest
     * @return
     */
    public Alert create(CreateAlertRequest createAlertRequest) {
        Alert alert = new Alert();
        alert.setCondition(createAlertRequest.condition());
        alert.setSymbol(createAlertRequest.symbol());
        alert.setUserEmail(createAlertRequest.userEmail());
        alert.setTargetPrice(createAlertRequest.targetPrice());
        return alertRepository.save(alert);
    }

    /**
     * Checks active alerts for the symbol given the current price and triggers them when matched.
     *
     * @param symbol
     * @param currentPrice
     */
    public void checkAlarms(String symbol, BigDecimal currentPrice) {
        List<Alert> activeAlerts = alertRepository.findBySymbolAndIsTriggeredFalse(symbol);

        for (Alert alert : activeAlerts) {
            boolean isTriggered = false;

            if (alert.getCondition() == AlertCondition.ABOVE && currentPrice.compareTo(alert.getTargetPrice()) >= 0) {
                isTriggered = true;
            } else if (alert.getCondition() == AlertCondition.BELOW && currentPrice.compareTo(alert.getTargetPrice()) <= 0) {
                isTriggered = true;
            }

            if (isTriggered) {
                alert.setTriggered(true);
                alertRepository.save(alert);

                notificationService.sendAlertNotification(alert.getUserEmail(), symbol, currentPrice);
            }
        }
    }
}
