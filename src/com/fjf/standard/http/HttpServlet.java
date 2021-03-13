package com.fjf.standard.http;

import com.fjf.standard.Servlet;
import com.fjf.standard.ServletException;
import com.fjf.standard.ServletRequest;
import com.fjf.standard.ServletResponse;

import java.io.IOException;

public abstract class HttpServlet implements Servlet {
    // 初始化Servlet
    @Override
    public void init() throws ServletException {
    }
    // 销毁 Servlet
    @Override
    public void destroy() {
    }

    // 处理 Servlet 服务
    @Override
    public void service(ServletRequest req, ServletResponse resp) throws ServletException, IOException {
        // 如果 Request 和 Response 都是 HTTP 类型的，那么就强转类型之后再调用重载的 service
        if (req instanceof HttpServletRequest && resp instanceof HttpServletResponse) {
            HttpServletRequest httpReq = (HttpServletRequest)req;
            HttpServletResponse httpResp = (HttpServletResponse)resp;
            // 根据参数不同调用重载的 service
            service(httpReq, httpResp);
        } else {
            throw new ServletException("不支持非 HTTP 协议");
        }
    }
    // 重载 service
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 只处理 GET 请求方法，否则就返回 405 错误码
        if (req.getMethod().equals("GET")) {
            doGet(req, resp);
        } else {
            resp.sendError(405);
        }
    }
    // 默认返回 405 错误码（没有重写 doGet），需要重写
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendError(405);
    }
}
