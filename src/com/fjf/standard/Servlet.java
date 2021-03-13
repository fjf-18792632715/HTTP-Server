package com.fjf.standard;

import java.io.IOException;

// Servlet接口标准
public interface Servlet {
    // 一次初始化
    void init() throws ServletException;
    // 多次service提供服务
    void service(ServletRequest req, ServletResponse resp) throws ServletException, IOException;
    // 一次销毁操作
    void destroy();
}
