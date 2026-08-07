package com.example.finance.controller;

import com.example.finance.legacy.LegacyBankAdapter;
import com.example.finance.legacy.LegacyBankAdapter.BankStatement;
import com.example.finance.legacy.LegacyBankAdapter.InternalEntry;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/legacy")
public class LegacyController {

    @PostMapping("/adapt")
    public Map<String, Object> adapt(
            @RequestParam String txnId,
            @RequestParam BigDecimal credit,
            @RequestParam BigDecimal debit) {
        BankStatement stmt = new BankStatement(txnId, credit, debit, "银行流水");
        InternalEntry entry = LegacyBankAdapter.adapt(stmt);
        Map<String, Object> r = new HashMap<>();
        r.put("txnId",          entry.getTxnId());
        r.put("debitAmount",    entry.getDebitAmount());
        r.put("creditAmount",   entry.getCreditAmount());

        r.put("correctDebit",   credit);
        r.put("correctCredit",  debit);
        return r;
    }
}
