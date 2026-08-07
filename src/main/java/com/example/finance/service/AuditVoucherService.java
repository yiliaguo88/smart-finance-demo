package com.example.finance.service;

import com.example.finance.model.VoucherRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class AuditVoucherService implements VoucherService {

    private final VoucherService    delegate;
    private final List<String>      auditLog = new ArrayList<>();

    public AuditVoucherService(VoucherServiceImpl delegate) {
        this.delegate = delegate;
    }

    @Override
    public Map<String, Object> submitVoucher(VoucherRequest req) {
        auditLog.add("SUBMIT attempt: company=" + req.getCompanyId()
                + " amount=" + req.getTotalAmount());
        Map<String, Object> result = delegate.submitVoucher(req);
        auditLog.add("SUBMIT result: " + result.get("status")
                + " voucherNo=" + result.get("voucherNo"));
        return result;
    }

    @Override
    public Map<String, Object> getVouchers(String companyId, String period) {
        return delegate.getVouchers(companyId, period);
    }

    public List<String> getAuditLog() {
        return Collections.unmodifiableList(auditLog);
    }
}
