package com.example.finance.service;

import com.example.finance.entity.Voucher;
import com.example.finance.model.VoucherRequest;
import com.example.finance.processor.ExpenseVoucherProcessor;
import com.example.finance.processor.VoucherProcessorTemplate.ProcessResult;
import com.example.finance.repository.VoucherMapper;
import com.example.finance.stats.FinanceStatisticsCollector;
import com.example.finance.validation.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VoucherServiceImpl implements VoucherService {

    private final VoucherMapper           voucherMapper;
    private final ExpenseVoucherProcessor processor;

    public VoucherServiceImpl(VoucherMapper voucherMapper,
                              ExpenseVoucherProcessor processor) {
        this.voucherMapper = voucherMapper;
        this.processor     = processor;
    }

    @Override
    public Map<String, Object> submitVoucher(VoucherRequest req) {
        VoucherValidator chain = buildValidatorChain();
        VoucherValidator.ValidationResult validation = chain.validate(req);

        Map<String, Object> result = new HashMap<>();
        if (!validation.isPassed()) {
            result.put("status", "rejected");
            result.put("reason", validation.getMessage());
            FinanceStatisticsCollector.getInstance().recordRejected();
            return result;
        }

        ProcessResult pr = processor.processVoucher(req);
        FinanceStatisticsCollector.getInstance().recordVoucher(req.getTotalAmount());

        result.put("status",    pr.isValid() ? "ok" : "error");
        result.put("voucherNo", pr.getVoucherNo());
        result.put("savedId",   pr.getSavedId());
        result.put("message",   pr.getMessage());
        return result;
    }

    @Override
    public Map<String, Object> getVouchers(String companyId, String period) {
        List<Voucher> list = voucherMapper.findByCompanyAndPeriod(companyId, period);

        double totalAmount = 0.0;
        for (Voucher v : list) {
            totalAmount += v.getTotalAmount().doubleValue();
        }

        Map<String, Object> result = new HashMap<>();
        result.put("vouchers",    list);
        result.put("totalAmount", totalAmount);
        result.put("count",       list.size());
        return result;
    }

    private VoucherValidator buildValidatorChain() {
        VoucherValidator format     = new FormatVoucherValidator();
        VoucherValidator balance    = new BalanceVoucherValidator();
        VoucherValidator compliance = new ComplianceVoucherValidator();
        format.setNext(balance).setNext(compliance);
        return format;
    }
}
