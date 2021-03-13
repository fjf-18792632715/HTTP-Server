package com.fjf.Tomcat.http;

import com.fjf.Tomcat.utils.SessionUtil;
import com.fjf.standard.http.HttpSession;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HttpSessionImpl implements HttpSession {

    // Session 内部就维护了一个 Map<String, Object> 对象，用于存储信息
    private final Map<String, Object> sessionData;
    private final String sessionID;

    @Override
    public String toString() {
        return "HttpSessionImpl{" +
                "sessionData=" + sessionData +
                ", sessionID='" + sessionID + '\'' +
                '}';
    }

    public HttpSessionImpl() {
        this.sessionID = UUID.randomUUID().toString();
        this.sessionData = new HashMap<>();
    }

    public HttpSessionImpl(String sessionID) {
        this.sessionID = sessionID;
        this.sessionData = SessionUtil.loadSession(sessionID);
    }

    @Override
    public Object getAttribute(String name) {
        // 通过键获取值
        return sessionData.get(name);
    }

    @Override
    public void removeAttribute(String name) {
        sessionData.remove(name);
    }

    @Override
    public void setAttribute(String name, Object value) {
        sessionData.put(name, value);
    }
}
