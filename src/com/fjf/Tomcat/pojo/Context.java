package com.fjf.Tomcat.pojo;

import com.fjf.standard.Servlet;
import com.fjf.standard.ServletException;

import java.util.*;

public class Context {
    private Config config;
    private String name;
    // web 应用中的所有类都使用这个类加载器加载。（每个 Context 都有属于自己的类加载器，互补干扰）
    private final ClassLoader webappClassLoader = Context.class.getClassLoader();

    // List 用来存放加载的类
    private final List<Class<?>> classList = new ArrayList<>();
    public void loadServletClasses() throws ClassNotFoundException {
        // 使用 Context 自带的类加载器对 Config 中所有的 Servlet 路径的类进行加载
        Set<String> servletClassNames = new HashSet<>(config.getServletNameToServletClassPath().values());
        for (String servletClass : servletClassNames) {
            Class<?> aClass = webappClassLoader.loadClass(servletClass);
            classList.add(aClass);
        }
    }

    // 实例化加载的类，并且将实例化的类 放在 List 中
    public List<Servlet> servletList = new ArrayList<>();
    public void instantiateServletObject() throws IllegalAccessException, InstantiationException {
        for (Class<?> servletClass : classList) {
            Servlet instance = (Servlet) servletClass.newInstance(); // 默认调用无参构造方法
            servletList.add(instance);
        }
    }

    // 初始化加载的 Servlet 类
    public void initializeServletObject() throws ServletException {
        for (Servlet servlet : servletList) {
            servlet.init();
        }
    }

    // 销毁 Servlet 类
    public void destroyServlet() {
        for (Servlet servlet : servletList) {
            servlet.destroy();
        }
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Context(String name) {
        this.name = name;
    }

}
