package com.fjf.standard.http;

public interface HttpSession {
    // 获取Session的内容字段
    Object getAttribute(String name);
    // 移除某字段
    void removeAttribute(String name);
    // 设置某字段
    void setAttribute(String name, Object value);
}
