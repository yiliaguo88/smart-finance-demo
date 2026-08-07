package com.example.finance.controller;

import com.example.finance.tax.TaxCalculator;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/tax")
public class TaxController {

    @GetMapping("/calculate")
    public Map<String, Object> calculate(
            @RequestParam long amount,
            @RequestParam(defaultValue = "13") int taxRate) {
        long tax = TaxCalculator.calcTaxFromInclusive(amount, taxRate);
        Map<String, Object> r = new HashMap<>();
        r.put("inclusiveAmount", amount);
        r.put("taxRate", taxRate);
        r.put("taxAmount", tax);

        long correct = Math.round(amount * (double) taxRate / (100 + taxRate));
        r.put("correctTaxAmount", correct);
        return r;
    }
}
