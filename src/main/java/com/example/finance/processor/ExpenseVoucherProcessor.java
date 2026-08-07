package com.example.finance.processor;

import com.example.finance.model.VoucherRequest;
import com.example.finance.repository.VoucherMapper;
import com.example.finance.entity.Voucher;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class ExpenseVoucherProcessor extends VoucherProcessorTemplate {

    private static final AtomicLong SEQ = new AtomicLong(1000);
    private final VoucherMapper voucherMapper;

    public ExpenseVoucherProcessor(VoucherMapper voucherMapper) {
        this.voucherMapper = voucherMapper;
    }

    @Override
    protected void enrichVoucher(VoucherRequest req, ProcessResult result) {
        String no = "EXP-" + req.getPeriod() + "-" + SEQ.getAndIncrement();
        result.setVoucherNo(no);
    }

    @Override
    protected void persistVoucher(VoucherRequest req, ProcessResult result) {
        Voucher v = new Voucher();
        v.setVoucherNo(result.getVoucherNo());
        v.setCompanyId(req.getCompanyId());
        v.setPeriod(req.getPeriod() != null ? req.getPeriod() : "2024-01");
        v.setVoucherType("EXPENSE");
        v.setTotalAmount(req.getTotalAmount());
        v.setStatus("DRAFT");
        v.setCreatedBy(req.getCreatedBy());
        v.setCreatedAt(LocalDateTime.now());
        voucherMapper.insert(v);
        result.setSavedId(v.getId());
    }
}
