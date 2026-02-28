package com.crypto.watcher.crypto_watcher.dto.response;

import java.math.BigDecimal;

public record CryptoPriceResponse(
        String symbol,
        BigDecimal targetPrice
) {
}
