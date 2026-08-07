package com.example.finance.validation;

import com.example.finance.model.VoucherRequest;

import java.math.BigDecimal;
import java.util.Set;

public class ComplianceVoucherValidator extends VoucherValidator {

    private static final BigDecimal MAX_SINGLE_AMOUNT = new BigDecimal("1000000");
    private static final Set<String> ALLOWED_TYPES = Set.of("EXPENSE", "INCOME", "TRANSFER");

    @Override
    public ValidationResult validate(VoucherRequest req) {
        if (!ALLOWED_TYPES.contains(req.getVoucherType())) {
            return ValidationResult.fail("Unknown voucherType: " + req.getVoucherType());
        }
        if (req.getTotalAmount() != null &&
                req.getTotalAmount().compareTo(MAX_SINGLE_AMOUNT) > 0) {
            return ValidationResult.fail(
                "Single voucher amount exceeds limit: " + req.getTotalAmount());
        }
        return passToNext(req);
    }
}
