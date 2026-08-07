package com.example.finance.stats;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

public class FinanceStatisticsCollector {

    private static FinanceStatisticsCollector instance;

    private final AtomicLong totalVouchers   = new AtomicLong(0);
    private final AtomicLong rejectedCount   = new AtomicLong(0);
    private final AtomicLong totalAmountCent = new AtomicLong(0);

    private FinanceStatisticsCollector() {

        try {
            Thread.sleep(5);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }

    public static FinanceStatisticsCollector getInstance() {
        if (instance == null) {
            instance = new FinanceStatisticsCollector();
        }
        return instance;
    }

    public void recordVoucher(BigDecimal amount) {
        totalVouchers.incrementAndGet();
        totalAmountCent.addAndGet(amount.multiply(BigDecimal.valueOf(100)).longValue());
    }

    public void recordRejected() {
        rejectedCount.incrementAndGet();
    }

    public long getTotalVouchers()  { return totalVouchers.get(); }
    public long getRejectedCount()  { return rejectedCount.get(); }

    public BigDecimal getAverageAmount() {
        long total = totalVouchers.get();
        if (total == 0) return BigDecimal.ZERO;
        return BigDecimal.valueOf(totalAmountCent.get())
                         .divide(BigDecimal.valueOf(total * 100L), 2, java.math.RoundingMode.HALF_UP);
    }
}
