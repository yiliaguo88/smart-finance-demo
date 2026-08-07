package com.example.finance.validation;

import com.example.finance.model.VoucherRequest;

public class FormatVoucherValidator extends VoucherValidator {

    @Override
    public ValidationResult validate(VoucherRequest req) {
        if (req.getCompanyId() == null || req.getCompanyId().isBlank())
            return ValidationResult.fail("companyId is required");
        if (req.getPeriod() == null || !req.getPeriod().matches("\\d{4}-\\d{2}"))
            return ValidationResult.fail("period must be YYYY-MM, got: " + req.getPeriod());
        if (req.getVoucherType() == null || req.getVoucherType().isBlank())
            return ValidationResult.fail("voucherType is required");
        if (req.getEntries() == null || req.getEntries().isEmpty())
            return ValidationResult.fail("at least one entry is required");
        return passToNext(req);
    }
}
