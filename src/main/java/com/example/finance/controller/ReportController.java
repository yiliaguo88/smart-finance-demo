package com.example.finance.controller;

import com.example.finance.exchange.ExchangeRateService;
import com.example.finance.ledger.LedgerBalanceService;
import com.example.finance.stats.FinanceStatisticsCollector;
import com.example.finance.repository.VoucherMapper;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    private final VoucherMapper       voucherMapper;
    private final LedgerBalanceService ledgerService;
    private final ExchangeRateService  fxService;

    public ReportController(VoucherMapper mapper,
                            LedgerBalanceService ledger,
                            ExchangeRateService fx) {
        this.voucherMapper = mapper;
        this.ledgerService = ledger;
        this.fxService     = fx;
    }

    @GetMapping("/stats")
    public Map<String, Object> stats() {
        FinanceStatisticsCollector c = FinanceStatisticsCollector.getInstance();
        Map<String, Object> r = new HashMap<>();
        r.put("totalVouchers",  c.getTotalVouchers());
        r.put("rejectedCount",  c.getRejectedCount());
        r.put("averageAmount",  c.getAverageAmount());
        r.put("voucherCount",   voucherMapper.findAll().size());
        return r;
    }

    @GetMapping("/ledger")
    public Map<String, Object> ledger() {
        Map<String, Object> r = new HashMap<>();
        r.put("balances", ledgerService.getAllNetBalances());
        r.put("usdToCnyRate", fxService.getUsdToCnyRate());
        return r;
    }

    @PostMapping("/ledger/post")
    public Map<String, Object> postEntry(@RequestParam String accountCode,
                                         @RequestParam BigDecimal debit,
                                         @RequestParam BigDecimal credit) {
        ledgerService.postEntry(accountCode, debit, credit);
        Map<String, Object> r = new HashMap<>();
        r.put("status", "ok");
        r.put("netBalance", ledgerService.getNetBalance(accountCode));
        return r;
    }

    @PostMapping("/ledger/trigger-bug-d")
    public Map<String, Object> triggerBugD() throws InterruptedException {
        String account = "BUG_D_TEST";
        int threads = 50;
        BigDecimal amount = new BigDecimal("100");

        ledgerService.clearAccount(account);

        ExecutorService pool = Executors.newFixedThreadPool(threads);
        CountDownLatch ready = new CountDownLatch(threads);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done  = new CountDownLatch(threads);

        for (int i = 0; i < threads; i++) {
            pool.submit(() -> {
                ready.countDown();
                try {
                    start.await();

                    ledgerService.postEntry(account, amount, BigDecimal.ZERO);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    done.countDown();
                }
            });
        }

        ready.await();
        start.countDown();
        done.await();
        pool.shutdown();

        BigDecimal expected = amount.multiply(new BigDecimal(threads));
        BigDecimal actual   = ledgerService.getNetBalance(account);
        boolean bugTriggered = actual.compareTo(expected) != 0;

        Map<String, Object> r = new HashMap<>();
        r.put("account",     account);
        r.put("threads",     threads);
        r.put("expected",    expected);
        r.put("actual",      actual);
        r.put("bugTriggered", bugTriggered);
        r.put("lost",        expected.subtract(actual));
        return r;
    }

    @PostMapping("/stats/trigger-bug-h")
    public Map<String, Object> triggerBugH() throws Exception {
        java.lang.reflect.Field field = FinanceStatisticsCollector.class.getDeclaredField("instance");
        field.setAccessible(true);

        int threads     = 30;
        int maxAttempts = 20;

        ExecutorService pool = Executors.newFixedThreadPool(threads);

        int distinctCount = 1;
        long actual       = 0;
        int attemptsUsed  = 0;

        try {
            for (int attempt = 1; attempt <= maxAttempts; attempt++) {
                attemptsUsed = attempt;
                field.set(null, null);

                CountDownLatch ready = new CountDownLatch(threads);
                CountDownLatch start = new CountDownLatch(1);
                CountDownLatch done  = new CountDownLatch(threads);
                Set<Integer> identities = Collections.newSetFromMap(new ConcurrentHashMap<>());

                for (int i = 0; i < threads; i++) {
                    pool.submit(() -> {
                        ready.countDown();
                        try {
                            start.await();

                            FinanceStatisticsCollector c = FinanceStatisticsCollector.getInstance();
                            identities.add(System.identityHashCode(c));
                            c.recordVoucher(BigDecimal.valueOf(100));
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        } finally {
                            done.countDown();
                        }
                    });
                }

                ready.await();
                start.countDown();
                done.await();

                distinctCount = identities.size();
                actual = FinanceStatisticsCollector.getInstance().getTotalVouchers();

                if (distinctCount > 1) break;
            }
        } finally {
            pool.shutdown();
        }

        boolean bugTriggered = distinctCount > 1;

        Map<String, Object> r = new LinkedHashMap<>();
        r.put("threads",       threads);
        r.put("expected",      threads);
        r.put("actual",        actual);
        r.put("instanceCount", distinctCount);
        r.put("bugTriggered",  bugTriggered);
        r.put("lost",          (long) threads - actual);
        r.put("attempts",      attemptsUsed);
        return r;
    }

    @GetMapping("/fx")
    public Map<String, Object> convertFx(@RequestParam BigDecimal amount,
                                          @RequestParam(defaultValue = "USD") String currency) {
        BigDecimal cny = "EUR".equals(currency)
                ? fxService.convertEurToCny(amount)
                : fxService.convertUsdToCny(amount);
        Map<String, Object> r = new HashMap<>();
        r.put("source", currency);
        r.put("amount", amount);
        r.put("cny",    cny);
        r.put("rate",   "EUR".equals(currency) ? fxService.getEurToCnyRate() : fxService.getUsdToCnyRate());
        return r;
    }
}
