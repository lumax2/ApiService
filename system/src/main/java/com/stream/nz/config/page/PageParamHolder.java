package com.stream.nz.config.page;

public class PageParamHolder {
    private static final ThreadLocal<PageParam> PAGE_PARAM_THREAD_LOCAL = new ThreadLocal<>();

    public static void set(PageParam pageParam) {
        PAGE_PARAM_THREAD_LOCAL.set(pageParam);
    }

    public static PageParam get() {
        PageParam pageParam = PAGE_PARAM_THREAD_LOCAL.get();
        return pageParam == null ? PageParam.create().pageNum(1).pageSize(10) : pageParam;
    }

    public static void remove() {
        PAGE_PARAM_THREAD_LOCAL.remove();
    }
}
