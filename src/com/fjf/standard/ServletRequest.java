package com.fjf.standard;

public interface ServletRequest {
    // 获取请求参数（表单提交的参数，格式为："name: value"）
    String getParameter(String name);
}
