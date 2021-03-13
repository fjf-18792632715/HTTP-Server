package com.fjf.standard.http;

import com.fjf.standard.ServletResponse;

public interface HttpServletResponse extends ServletResponse {
    // 添加 Cookie
    void addCookie(Cookie cookie);
    // 设置错误码
    void sendError(int sc);
    // 重定向，location为重定向地址
    void sendRedirect(String location);
    // 设置响应头信息（"Set-Cookie: username=fjf\r\n"）（"Content-Type: text.html\r\n"）
    void setHeader(String name, String value);
    // 设置状态码
    void setStatus(int sc);
}
