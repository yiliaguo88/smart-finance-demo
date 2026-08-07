package com.example.finance.entity;

import java.math.BigDecimal;

public class VoucherEntry {
    private Long       id;
    private Long       voucherId;
    private String     accountCode;
    private String     accountName;
    private BigDecimal debitAmount;
    private BigDecimal creditAmount;
    private String     remark;

    public Long       getId()                        { return id; }
    public void       setId(Long id)                 { this.id = id; }
    public Long       getVoucherId()                 { return voucherId; }
    public void       setVoucherId(Long v)           { this.voucherId = v; }
    public String     getAccountCode()               { return accountCode; }
    public void       setAccountCode(String c)       { this.accountCode = c; }
    public String     getAccountName()               { return accountName; }
    public void       setAccountName(String n)       { this.accountName = n; }
    public BigDecimal getDebitAmount()               { return debitAmount; }
    public void       setDebitAmount(BigDecimal d)   { this.debitAmount = d; }
    public BigDecimal getCreditAmount()              { return creditAmount; }
    public void       setCreditAmount(BigDecimal c)  { this.creditAmount = c; }
    public String     getRemark()                    { return remark; }
    public void       setRemark(String r)            { this.remark = r; }
}
