package com.fjf.webapps.dictionary;

import com.fjf.standard.ServletException;
import com.fjf.standard.http.HttpServlet;
import com.fjf.standard.http.HttpServletRequest;
import com.fjf.standard.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class TranslateServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        System.out.println("I am Init()");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String english = req.getParameter("english");

        String chinese = translate(english);

        resp.setContentType("text/html");
        PrintWriter writer = resp.getWriter();
        writer.printf("<h1>翻译服务</h1>\r\n");
        writer.printf("<p>%s 的意思是 %s</p>\r\n", "english", chinese);
//        writer.flush();
        System.out.println("我是 TranslateServlet 的 doGet 方法");

    }

    private String translate(String english) {
        return english;
    }
}
