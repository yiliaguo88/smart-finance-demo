package com.example.finance.exchange;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ExchangeRateService {

    private static final Logger log = LoggerFactory.getLogger(ExchangeRateService.class);

    private double usdToCnyRate = 7.10;
    private double eurToCnyRate = 7.75;

    @Scheduled(fixedRate = 60_000)
    public void refreshRates() {

        double newUsd = 7.10 + (Math.random() * 0.30 - 0.15);
        double newEur = 7.75 + (Math.random() * 0.40 - 0.20);
        usdToCnyRate = newUsd;
        eurToCnyRate = newEur;
        log.info("Rates refreshed: USD/CNY={:.4f}, EUR/CNY={:.4f}", newUsd, newEur);
    }

    public BigDecimal convertUsdToCny(BigDecimal usdAmount) {
        return usdAmount.multiply(BigDecimal.valueOf(usdToCnyRate))
                        .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal convertEurToCny(BigDecimal eurAmount) {
        return eurAmount.multiply(BigDecimal.valueOf(eurToCnyRate))
                        .setScale(2, RoundingMode.HALF_UP);
    }

    public double getUsdToCnyRate() { return usdToCnyRate; }
    public double getEurToCnyRate() { return eurToCnyRate; }
}
