package com.example.finance.controller;

import com.example.finance.context.TenantContextHolder;
import com.example.finance.model.VoucherRequest;
import com.example.finance.service.AuditVoucherService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/vouchers")
public class VoucherController {

    private final AuditVoucherService voucherService;

    public VoucherController(AuditVoucherService voucherService) {
        this.voucherService = voucherService;
    }

    @PostMapping
    public Map<String, Object> submit(@RequestBody VoucherRequest req) {
        TenantContextHolder.TenantContext ctx = TenantContextHolder.get();
        if (ctx != null && req.getCompanyId() == null) {
            req.setCompanyId(ctx.getCompanyId());
        }
        return voucherService.submitVoucher(req);
    }

    @GetMapping
    public Map<String, Object> list(@RequestParam String companyId,
                                    @RequestParam(defaultValue = "2024-01") String period) {
        return voucherService.getVouchers(companyId, period);
    }

    @GetMapping("/mine")
    public Map<String, Object> listMine(@RequestParam(defaultValue = "2024-01") String period) {
        TenantContextHolder.TenantContext ctx = TenantContextHolder.get();
        if (ctx == null) {
            return Map.of("error", "No tenant context found");
        }

        return voucherService.getVouchers(ctx.getCompanyId(), period);
    }

    @GetMapping("/audit-log")
    public java.util.List<String> auditLog() {
        return voucherService.getAuditLog();
    }
}
