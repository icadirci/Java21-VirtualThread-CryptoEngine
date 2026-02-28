package com.crypto.watcher.crypto_watcher.dto;

import java.math.BigDecimal;

public record BinancePriceRecord(String symbol, BigDecimal price) {
}
