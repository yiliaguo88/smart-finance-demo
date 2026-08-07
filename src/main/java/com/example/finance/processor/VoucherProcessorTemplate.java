package com.example.finance.processor;

import com.example.finance.model.VoucherRequest;

public abstract class VoucherProcessorTemplate {

    public final ProcessResult processVoucher(VoucherRequest req) {
        ProcessResult result = new ProcessResult();

        validateVoucher(req, result);
        if (!result.isValid()) return result;

        enrichVoucher(req, result);
        persistVoucher(req, result);
        notifyListeners(req, result);
        return result;
    }

    protected void validateVoucher(VoucherRequest req, ProcessResult result) {
        if (req.getCompanyId() == null || req.getCompanyId().isBlank()) {
            result.setValid(false);
            result.setMessage("companyId is required");
            return;
        }
        if (req.getTotalAmount() == null ||
                req.getTotalAmount().signum() <= 0) {
            result.setValid(false);
            result.setMessage("totalAmount must be positive");
            return;
        }
        result.setValid(true);
    }

    protected abstract void enrichVoucher(VoucherRequest req, ProcessResult result);

    protected abstract void persistVoucher(VoucherRequest req, ProcessResult result);

    protected void notifyListeners(VoucherRequest req, ProcessResult result) {
        result.setMessage("Voucher processed for company: " + req.getCompanyId());
    }

    public static class ProcessResult {
        private boolean valid   = true;
        private String  message = "";
        private String  voucherNo;
        private Long    savedId;

        public boolean isValid()            { return valid; }
        public void    setValid(boolean v)  { this.valid = v; }
        public String  getMessage()         { return message; }
        public void    setMessage(String m) { this.message = m; }
        public String  getVoucherNo()       { return voucherNo; }
        public void    setVoucherNo(String n){ this.voucherNo = n; }
        public Long    getSavedId()         { return savedId; }
        public void    setSavedId(Long id)  { this.savedId = id; }
    }
}
