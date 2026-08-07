package com.example.finance.validation;

import com.example.finance.model.VoucherRequest;

public abstract class VoucherValidator {

    protected VoucherValidator next;

    public VoucherValidator setNext(VoucherValidator next) {
        this.next = next;
        return next;
    }

    public abstract ValidationResult validate(VoucherRequest req);

    protected ValidationResult passToNext(VoucherRequest req) {
        if (next != null) return next.validate(req);
        return ValidationResult.ok("All validations passed");
    }

    public static class ValidationResult {
        private final boolean passed;
        private final String  message;

        private ValidationResult(boolean passed, String message) {
            this.passed  = passed;
            this.message = message;
        }

        public static ValidationResult ok(String msg)   { return new ValidationResult(true,  msg); }
        public static ValidationResult fail(String msg) { return new ValidationResult(false, msg); }

        public boolean isPassed()   { return passed; }
        public String  getMessage() { return message; }
    }
}
