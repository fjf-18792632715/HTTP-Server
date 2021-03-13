package com.fjf.Tomcat.http;

import com.fjf.standard.http.Cookie;
import com.fjf.standard.http.HttpServletRequest;
import com.fjf.standard.http.HttpSession;

import java.util.List;
import java.util.Map;

public class Request implements HttpServletRequest {

    private final String requestMethod;
    private final String contextPath;
    private final String servletPath;
    private final String requestURI;
    private final Map<String, String> parameters;
    private final Map<String, String> headers;
    private final List<Cookie> cookies;
    private HttpSession session = null;

    public Request(String requestMethod, String contextPath, String servletPath, String requestURI, Map<String, String> parameters, Map<String, String> headers, List<Cookie> cookies) {
        this.requestMethod = requestMethod;
        this.contextPath = contextPath;
        this.servletPath = servletPath;
        this.requestURI = requestURI;
        this.parameters = parameters;
        this.headers = headers;
        this.cookies = cookies;
        String id = "session-id";
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(id)) {
                // 在 Cookie 中找到 sessionID，实例化 HttpSession 对象
                this.session = new HttpSessionImpl(cookie.getValue());
                break;
            }
        }
        // 没找到 sessionID，说明没有携带 Session 实例化 HttpSession 对象
        if (this.session == null) {
            this.session = new HttpSessionImpl();
        }
    }

    /**
     * @return
     */
    @Override
    public String toString() {
        return "Request{" +
                "requestMethod='" + requestMethod + '\'' +
                ", contextPath='" + contextPath + '\'' +
                ", servletPath='" + servletPath + '\'' +
                ", requestURI='" + requestURI + '\'' +
                ", parameters=" + parameters +
                ", headers=" + headers +
                ", cookies=" + cookies +
                ", session=" + session +
                '}';
    }

    @Override
    public Cookie[] getCookies() {
//        Cookie[] res = new Cookie[cookies.size()];
//        int index = 0;
//        for (Cookie cookie : cookies) {
//            res[index++] = cookie;
//        }
        return cookies.toArray(new Cookie[0]);
    }

    @Override
    public String getHeader(String name) {
        return headers.get(name);
    }

    @Override
    public String getMethod() {
        return requestMethod;
    }

    @Override
    public String getContextPath() {
        return contextPath;
    }

    @Override
    public String getServletPath() {
        return servletPath;
    }

    @Override
    public String getRequestURI() {
        return requestURI;
    }

    @Override
    public HttpSession getSession() {
        return session;
    }

    @Override
    public String getParameter(String name) {
        return parameters.get(name);
    }
}
