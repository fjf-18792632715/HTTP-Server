package com.fjf.Tomcat.utils;

import com.fjf.Tomcat.http.Request;
import com.fjf.standard.http.Cookie;
import com.sun.xml.internal.ws.wsdl.writer.document.OpenAtts;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URLDecoder;
import java.nio.file.Path;
import java.util.*;

// 解析请求为 Request 对象
public class HttpRequestParser {

    public Request parse(InputStream socketInputStream) throws UnsupportedEncodingException {
        Scanner sc = new Scanner(socketInputStream, "UTF-8");

        String requestMethod = sc.next().toUpperCase();

        // 请求行会携带 URI、ServletPath、ContextPath 还有 Parameters，所以需要对其进行解析
        String requestPath = sc.next();
        Map<String, String> parameters = new HashMap<>();
        // RequestURI
        String requestURI = requestPath;
        String contextPath = "/";
        String servletPath = null;
        // 找到 ？ 的位置
        int i = requestPath.indexOf("?");
        if (i != -1) {
            requestURI = requestPath.substring(0, i);
            // queryString 就是类似于  username=fjf&age=17&sex=1
            String queryString = requestPath.substring(i + 1);
            String[] split = queryString.split("&");
            for (String queryKV : split) {
                String[] kv = queryKV.split("=");
                String name = URLDecoder.decode(kv[0].trim(), "UTF-8");
                String value = URLDecoder.decode(kv[1].trim(), "UTF-8");
                parameters.put(name, value);
            }
        }
        // 找到 第二个 “/” 的位置并返回索引位置
        servletPath = requestURI;
        int index = requestPath.indexOf("/", 1);
        if (index != -1) {
            // ContextPath
            contextPath = requestPath.substring(1, index);
            // ServletPath
            servletPath = requestPath.substring(index);
            if (i != -1) {
                servletPath = requestPath.substring(index, i);
            }
        }

        String requestVersion = sc.nextLine();

        // 用于存储请求头
        Map<String, String> requestHeader = new HashMap<>();
        // 用于存储请求头中包含的 cookie 键值对
        List<Cookie> cookies = new ArrayList<>();

        String header = null;
        while (sc.hasNextLine() && !(header = sc.nextLine()).trim().isEmpty()) {
            String[] split = header.split(":");
            String headerName = split[0].trim().toLowerCase();
            String headerValue = split[1].trim();
            requestHeader.put(headerName, headerValue);

            // 如果请求头的 Name 为 cookie 时，说明解析到 Cookie 请求头了，解析为 cookie
            if (headerName.equals("cookie")) {
                String[] cookieKVs = headerValue.split(";");
                for (String s : cookieKVs) {
                    if (s.trim().isEmpty()) {
                        continue;
                    }
                    String[] KV = s.split("=");
                    Cookie cookie = new Cookie(KV[0].trim(), KV[1].trim());
                    cookies.add(cookie);
                }
            }
        }

        return new Request(requestMethod, contextPath, servletPath, requestPath, parameters, requestHeader, cookies);
    }

}
