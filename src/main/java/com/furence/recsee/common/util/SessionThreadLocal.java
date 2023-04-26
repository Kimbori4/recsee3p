package com.furence.recsee.common.util;

import javax.servlet.http.HttpSession;

public class SessionThreadLocal {
    private static final ThreadLocal<HttpSession> threadLocal;

    static {
        threadLocal = new ThreadLocal<>();
    }

    public static void set(HttpSession session) {
        threadLocal.set(session);
    }

    public static void unset() {
        threadLocal.remove();
    }

    public static HttpSession get() {
        return threadLocal.get();
    }
}
