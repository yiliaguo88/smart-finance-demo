package com.example.finance.context;

public class TenantContextHolder {

    private static final ThreadLocal<TenantContext> CONTEXT = new ThreadLocal<>();

    public static void set(TenantContext ctx) {
        CONTEXT.set(ctx);
    }

    public static TenantContext get() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }

    public static class TenantContext {
        private final String companyId;
        private final String operatorCode;
        private final String period;
        private final long   requestStartMs;

        public TenantContext(String companyId, String operatorCode, String period) {
            this.companyId     = companyId;
            this.operatorCode  = operatorCode;
            this.period        = period;
            this.requestStartMs = System.currentTimeMillis();
        }

        public String getCompanyId()     { return companyId; }
        public String getOperatorCode()  { return operatorCode; }
        public String getPeriod()        { return period; }
        public long   getRequestStartMs(){ return requestStartMs; }
    }
}
