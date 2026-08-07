package com.example.finance.ledger;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LedgerBalanceService {

    private final Map<String, BigDecimal> debitBalances  = new HashMap<>();
    private final Map<String, BigDecimal> creditBalances = new HashMap<>();

    @PostConstruct
    public void initSeedData() {

        debitBalances.put("1001", new BigDecimal("20000.00"));
        debitBalances.put("1002", new BigDecimal("350000.00"));
        debitBalances.put("1121", new BigDecimal("80000.00"));

        creditBalances.put("2202", new BigDecimal("45000.00"));
        creditBalances.put("5001", new BigDecimal("120000.00"));
    }

    public void postEntry(String accountCode, BigDecimal debit, BigDecimal credit) {
        if (debit.compareTo(BigDecimal.ZERO) > 0) {
            debitBalances.merge(accountCode, debit, BigDecimal::add);
        }
        if (credit.compareTo(BigDecimal.ZERO) > 0) {
            creditBalances.merge(accountCode, credit, BigDecimal::add);
        }
    }

    public BigDecimal getNetBalance(String accountCode) {
        BigDecimal debit  = debitBalances.getOrDefault(accountCode, BigDecimal.ZERO);
        BigDecimal credit = creditBalances.getOrDefault(accountCode, BigDecimal.ZERO);
        return debit.subtract(credit);
    }

    public Map<String, BigDecimal> getAllNetBalances() {
        Set<String> allCodes = new HashSet<>();
        allCodes.addAll(debitBalances.keySet());
        allCodes.addAll(creditBalances.keySet());

        return allCodes.stream().collect(Collectors.toMap(
                code -> code,
                this::getNetBalance
        ));
    }

    public void clearAccount(String accountCode) {
        debitBalances.remove(accountCode);
        creditBalances.remove(accountCode);
    }

    public void clear() {
        debitBalances.clear();
        creditBalances.clear();
    }
}
