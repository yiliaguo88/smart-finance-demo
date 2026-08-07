package com.example.finance.legacy;

import java.math.BigDecimal;

public class LegacyBankAdapter {

    public static class BankStatement {
        private final String     txnId;
        private final BigDecimal creditAmount;
        private final BigDecimal debitAmount;
        private final String     description;

        public BankStatement(String txnId, BigDecimal credit, BigDecimal debit, String desc) {
            this.txnId        = txnId;
            this.creditAmount = credit;
            this.debitAmount  = debit;
            this.description  = desc;
        }

        public String     getTxnId()        { return txnId; }
        public BigDecimal getCreditAmount() { return creditAmount; }
        public BigDecimal getDebitAmount()  { return debitAmount; }
        public String     getDescription()  { return description; }
    }

    public static class InternalEntry {
        private final String     txnId;
        private final BigDecimal debitAmount;
        private final BigDecimal creditAmount;

        public InternalEntry(String txnId, BigDecimal debit, BigDecimal credit) {
            this.txnId        = txnId;
            this.debitAmount  = debit;
            this.creditAmount = credit;
        }

        public String     getTxnId()        { return txnId; }
        public BigDecimal getDebitAmount()  { return debitAmount; }
        public BigDecimal getCreditAmount() { return creditAmount; }
    }

    public static InternalEntry adapt(BankStatement stmt) {
        return new InternalEntry(
            stmt.getTxnId(),
            stmt.getCreditAmount(),
            stmt.getDebitAmount()
        );
    }
}
