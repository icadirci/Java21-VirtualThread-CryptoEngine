package com.crypto.watcher.crypto_watcher.controller;


import com.crypto.watcher.crypto_watcher.dto.request.CreateAlertRequest;
import com.crypto.watcher.crypto_watcher.entity.Alert;
import com.crypto.watcher.crypto_watcher.service.AlertService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/alerts")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping
    public ResponseEntity<List<Alert>> allAlerts(){
        return ResponseEntity.ok().body(alertService.allAlerts());
    }

    @PostMapping()
    public ResponseEntity<Alert> addAlert(@RequestBody @Valid CreateAlertRequest alert){
        return ResponseEntity.ok().body(alertService.create(alert));
    }

}
