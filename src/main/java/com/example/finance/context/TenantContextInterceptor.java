package com.example.finance.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TenantContextInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(TenantContextInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) {
        String companyId    = req.getHeader("X-Company-Id");
        String operatorCode = req.getHeader("X-Operator-Code");
        String period       = req.getHeader("X-Period");

        if (period == null) period = "2024-01";

        if (companyId != null && TenantContextHolder.get() == null) {
            TenantContextHolder.set(
                new TenantContextHolder.TenantContext(companyId, operatorCode, period)
            );
            log.debug("Tenant set: companyId={}", companyId);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse resp,
                           Object handler, org.springframework.web.servlet.ModelAndView mv) {
    }

    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse resp,
                                Object handler, Exception ex) {

    }
}
