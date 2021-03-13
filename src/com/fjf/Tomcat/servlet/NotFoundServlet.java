package com.fjf.Tomcat.servlet;

import com.fjf.standard.ServletException;
import com.fjf.standard.http.HttpServlet;
import com.fjf.standard.http.HttpServletRequest;
import com.fjf.standard.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class NotFoundServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("NotFoundServlet 处理 404问题");

        resp.setStatus(404);
        resp.setContentType("text/html");
        PrintWriter writer = resp.getWriter();
        writer.println("<h1>该资源不存在</h1>");
    }
}
