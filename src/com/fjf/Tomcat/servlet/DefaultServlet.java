package com.fjf.Tomcat.servlet;

import com.fjf.Tomcat.HttpServer;
import com.fjf.standard.ServletException;
import com.fjf.standard.http.HttpServlet;
import com.fjf.standard.http.HttpServletRequest;
import com.fjf.standard.http.HttpServletResponse;
import jdk.internal.util.xml.impl.Input;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

// 用来处理没找到Servlet，请求静态资源，如果静态资源也不存在那么就返回 404 NotFound
public class DefaultServlet extends HttpServlet {
    // 默认页面（就是 servletPath 为 “/” 的时候，自动跳转到 login.html ）
    private final String welComeFile = "login.html";
    // mime 是指将 Content-Type 和文件后缀名对应起来（例如：.html -> "text/html"）
    private final Map<String, String> mime = new HashMap<>();

    @Override
    public void init() throws ServletException {
        mime.put("htm", "text/html");
        mime.put("html", "text/html");
        mime.put("jpg", "image/jpeg");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("DefaultServlet  处理 静态资源");

        String contextPath = req.getContextPath();
        String servletPath = req.getServletPath();

        // 如果 servletPath 是斜杠，那么就定位到 login.html 页面
        if (servletPath.equals("/")) {
            servletPath += welComeFile;
        }

        String fileName = String.format("%s/%s/%s", HttpServer.WEBAPPS_BASE, contextPath, servletPath);
        File file = new File(fileName);
        if (!file.exists()) {
            // 静态资源文件不存在  404
            HttpServer.notFoundServlet.service(req,resp);  // 进行了转发，让其他 Servlet 去处理这个请求
            return;
        }

        String contentType = mime.getOrDefault(servletPath.substring(servletPath.lastIndexOf('.') + 1), "text/plain");
        resp.setContentType(contentType);

        // 开始读文件并且写入 resp 的 OutputStream 流中
        OutputStream outputStream = resp.getOutputStream();
        try (InputStream inputStream = new FileInputStream(file)){
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.flush();
        }
    }
}
