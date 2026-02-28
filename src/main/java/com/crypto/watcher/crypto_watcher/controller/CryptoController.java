package com.crypto.watcher.crypto_watcher.controller;

import com.crypto.watcher.crypto_watcher.dto.response.CryptoPriceResponse;
import com.crypto.watcher.crypto_watcher.service.CryptoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/prices")
public class CryptoController {
    private final CryptoService cryptoService;

    public CryptoController(CryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    @GetMapping("/{symbol}")
    public ResponseEntity<CryptoPriceResponse> getSymbolPrice(@PathVariable String symbol){
        return ResponseEntity.ok().body(cryptoService.getPrice(symbol));
    }

}
