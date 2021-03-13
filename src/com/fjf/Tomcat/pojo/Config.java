package com.fjf.Tomcat.pojo;

import java.util.HashMap;
import java.util.Map;

public class Config {
    private Map<String, String> servletNameToServletClassPath;
    private  Map<String, String> urlToServletName;

    public Config(Map<String, String> servletNameToServletClassPath, Map<String, String> urlToServletName) {
        this.servletNameToServletClassPath = servletNameToServletClassPath;
        this.urlToServletName = urlToServletName;
    }

    public Map<String, String> getServletNameToServletClassPath() {
        return servletNameToServletClassPath;
    }

    public void setServletNameToServletClassPath(Map<String, String> servletNameToServletClassPath) {
        this.servletNameToServletClassPath = servletNameToServletClassPath;
    }

    public Map<String, String> getUrlToServletName() {
        return urlToServletName;
    }

    public void setUrlToServletName(Map<String, String> urlToServletName) {
        this.urlToServletName = urlToServletName;
    }


}
