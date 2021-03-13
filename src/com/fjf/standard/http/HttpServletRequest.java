package com.fjf.standard.http;

import com.fjf.standard.ServletRequest;

public interface HttpServletRequest extends ServletRequest {
    // 获取请求的 Cookies
    Cookie[] getCookies();
    // 获取请求头信息
    String getHeader(String name);
    // 获取请求方法（GET/POST）
    String getMethod();

    // 下面三个方法返回例子：       http://localhost:8080/fjf/admin/login?username=fff
    String getContextPath(); //    "/fjf"  为到 Context 目录的路径（也就是项目的名字）
    String getServletPath(); //    "/admin/login"   为进入 Context 目录后的路径和Servlet请求
    String getRequestURI(); //     "/fjf/admin/login"
    // 获取 Session
    HttpSession getSession();
}
