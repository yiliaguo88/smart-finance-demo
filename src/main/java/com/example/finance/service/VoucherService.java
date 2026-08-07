package com.example.finance.service;

import com.example.finance.model.VoucherRequest;

import java.util.Map;

public interface VoucherService {
    Map<String, Object> submitVoucher(VoucherRequest req);
    Map<String, Object> getVouchers(String companyId, String period);
}
