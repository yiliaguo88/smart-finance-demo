package com.example.finance.validation;

import com.example.finance.model.VoucherRequest;

import java.math.BigDecimal;

public class BalanceVoucherValidator extends VoucherValidator {

    @Override
    public ValidationResult validate(VoucherRequest req) {
        BigDecimal totalDebit  = BigDecimal.ZERO;
        BigDecimal totalCredit = BigDecimal.ZERO;

        for (VoucherRequest.EntryDto e : req.getEntries()) {
            if (e.getDebitAmount()  != null) totalDebit  = totalDebit.add(e.getDebitAmount());
            if (e.getCreditAmount() != null) totalCredit = totalCredit.add(e.getCreditAmount());
        }

        if (totalDebit.compareTo(totalCredit) != 0) {
            return ValidationResult.fail(
                String.format("Debit/Credit imbalance: debit=%s, credit=%s",
                    totalDebit, totalCredit));
        }
        return passToNext(req);
    }
}
