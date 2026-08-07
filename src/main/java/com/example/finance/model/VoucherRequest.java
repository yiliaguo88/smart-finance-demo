package com.example.finance.model;

import java.math.BigDecimal;
import java.util.List;

public class VoucherRequest {
    private String        companyId;
    private String        period;
    private String        voucherType;
    private BigDecimal    totalAmount;
    private String        createdBy;
    private List<EntryDto> entries;

    public String        getCompanyId()   { return companyId; }
    public void          setCompanyId(String c) { this.companyId = c; }
    public String        getPeriod()      { return period; }
    public void          setPeriod(String p) { this.period = p; }
    public String        getVoucherType() { return voucherType; }
    public void          setVoucherType(String t) { this.voucherType = t; }
    public BigDecimal    getTotalAmount() { return totalAmount; }
    public void          setTotalAmount(BigDecimal a) { this.totalAmount = a; }
    public String        getCreatedBy()   { return createdBy; }
    public void          setCreatedBy(String c) { this.createdBy = c; }
    public List<EntryDto> getEntries()   { return entries; }
    public void           setEntries(List<EntryDto> e) { this.entries = e; }

    public static class EntryDto {
        private String     accountCode;
        private String     accountName;
        private BigDecimal debitAmount;
        private BigDecimal creditAmount;
        private String     remark;

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
}
