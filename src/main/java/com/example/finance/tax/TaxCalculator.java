package com.example.finance.tax;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TaxCalculator {

    public static long calcTaxFromInclusive(long inclusiveAmount, int taxRate) {

        return inclusiveAmount * (taxRate / (100 + taxRate));
    }

    public static BigDecimal calcTaxFromExclusive(BigDecimal exclusiveAmount, int taxRate) {

        return exclusiveAmount
                .multiply(BigDecimal.valueOf(taxRate))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    public static BigDecimal toInclusive(BigDecimal exclusive, int taxRate) {
        BigDecimal rate = BigDecimal.valueOf(taxRate).divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        return exclusive.multiply(BigDecimal.ONE.add(rate)).setScale(2, RoundingMode.HALF_UP);
    }
}
