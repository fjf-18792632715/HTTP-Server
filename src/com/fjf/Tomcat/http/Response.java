package com.fjf.Tomcat.http;

import com.fjf.standard.http.Cookie;
import com.fjf.standard.http.HttpServletResponse;
import com.sun.org.apache.regexp.internal.RE;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class Response implements HttpServletResponse {
    // Cookie 信息、状态码、响应体（两种写入）
    public final List<Cookie> cookieList;
    private final Map<String, String> headers;
    private int status = 200;
    public final ByteArrayOutputStream bodyOutputStream;
    public final PrintWriter bodyPrintWriter;

    public Response() throws UnsupportedEncodingException {
        this.headers = new HashMap<>();
        this.cookieList = new ArrayList<>();
        // 写到一个 byte 类型的数组中(子节流)，参数是默认容量，多的时候回自动扩容
        this.bodyOutputStream = new ByteArrayOutputStream(1024);
        // 转换流，吧子节流转化为字符流，并且设置编码格式为 UTF-8
        Writer writer = new OutputStreamWriter(bodyOutputStream, "UTF-8");
        this.bodyPrintWriter = new PrintWriter(writer);
    }

    @Override
    public String toString() {
        return "Response{" +
                "cookieList=" + cookieList +
                ", headers=" + headers +
                ", status=" + status +
                ", bodyOutputStream=" + bodyOutputStream.toString() +
                '}';
    }

    @Override
    public void addCookie(Cookie cookie) {
        cookieList.add(cookie);
    }

    @Override
    public void sendError(int sc) {
        // TODO
    }

    // 重定向
    @Override
    public void sendRedirect(String location) {
        setStatus(307);
        setHeader("Location", location);
    }

    @Override
    public void setHeader(String name, String value) {
        headers.put(name, value);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public void setStatus(int sc) {
        this.status = sc;
    }

    public int getStatus(){
        return status;
    }

    // 写入响应体（byte）
    @Override
    public OutputStream getOutputStream() {
        return bodyOutputStream;
    }

    // 写入响应体（text）
    @Override
    public PrintWriter getWriter() throws IOException {
        return bodyPrintWriter;
    }

    @Override
    public void setContentType(String type) {
        if (type.startsWith("text/")) {
            type += "; charset=utf-8";
        }
        // 将 Content-type 保存在响应头中
        setHeader("Content-Type", type);
    }
}
