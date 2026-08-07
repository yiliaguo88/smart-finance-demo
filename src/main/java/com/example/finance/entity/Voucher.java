package com.example.finance.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Voucher {
    private Long          id;
    private String        voucherNo;
    private String        companyId;
    private String        period;
    private String        voucherType;
    private BigDecimal    totalAmount;
    private String        status;
    private String        createdBy;
    private LocalDateTime createdAt;

    public Long          getId()           { return id; }
    public void          setId(Long id)    { this.id = id; }
    public String        getVoucherNo()    { return voucherNo; }
    public void          setVoucherNo(String v) { this.voucherNo = v; }
    public String        getCompanyId()    { return companyId; }
    public void          setCompanyId(String c) { this.companyId = c; }
    public String        getPeriod()       { return period; }
    public void          setPeriod(String p) { this.period = p; }
    public String        getVoucherType()  { return voucherType; }
    public void          setVoucherType(String t) { this.voucherType = t; }
    public BigDecimal    getTotalAmount()  { return totalAmount; }
    public void          setTotalAmount(BigDecimal a) { this.totalAmount = a; }
    public String        getStatus()       { return status; }
    public void          setStatus(String s) { this.status = s; }
    public String        getCreatedBy()    { return createdBy; }
    public void          setCreatedBy(String c) { this.createdBy = c; }
    public LocalDateTime getCreatedAt()   { return createdAt; }
    public void          setCreatedAt(LocalDateTime t) { this.createdAt = t; }
}
