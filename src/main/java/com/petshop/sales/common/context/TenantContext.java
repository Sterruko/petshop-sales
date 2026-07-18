package com.petshop.sales.common.context;

public class TenantContext {
    private static final ThreadLocal<Long> companyId = new ThreadLocal<>();

    public static void setCompanyId(Long id) { companyId.set(id); }
    public static Long getCompanyId() { return companyId.get(); }
    public static void clear() { companyId.remove(); }
}
