package com.crypto.watcher.crypto_watcher;

import com.crypto.watcher.crypto_watcher.service.CryptoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestClient;

@SpringBootApplication
@Slf4j
@EnableScheduling
public class CryptoWatcherApplication {

	public static void main(String[] args) {
		SpringApplication.run(CryptoWatcherApplication.class, args);
	}

    @Bean
    public RestClient restClient() {
        return RestClient.create();
    }
}
