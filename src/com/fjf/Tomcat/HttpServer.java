package com.fjf.Tomcat;

import com.fjf.Tomcat.pojo.Context;
import com.fjf.Tomcat.servlet.DefaultServlet;
import com.fjf.Tomcat.servlet.NotFoundServlet;
import com.fjf.Tomcat.utils.ConfigReader;
import com.fjf.standard.Servlet;
import com.fjf.standard.ServletException;
import com.fjf.反射Demo.Demo;
import org.junit.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// 实现 Tomcat 服务器主函数，运行就等于开启服务器
public class HttpServer {

    public static final DefaultServlet defaultServlet = new DefaultServlet();
    public static final NotFoundServlet notFoundServlet = new NotFoundServlet();


    public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, ServletException {
        // 1. 找到所有 Servlet 对象，进行初始化
        initServer();

        // 2. 处理服务器逻辑
        startServer();

        // 3. 找到所有的Servlet对象，进行销毁
        destroyServlet();
    }

    // 1. 找到所有 Servlet 对象，进行初始化
    private static void initServer() throws ClassNotFoundException, IllegalAccessException, InstantiationException, ServletException {
        // 1.1 扫描找到所有 Contexts（项目文件夹（webapps/dictionary））
        scanContexts();
        // 1.2 解析 /WEB-INF/web.conf 内容
        parseContextConf();
        // 1.3 加载 Servlet Class 类
        loadServletClasses();
        // 1.4 实例化 Servlet 对象
        instantiateServletObject();
        // 1.5 初始化 Servlet 对象
        initializeServletObject();
    }

    public static final String WEBAPPS_BASE = "E:\\IDEA2020\\java-object\\HTTP-Server\\webapps";
    public static final List<Context> contextList = new ArrayList<>();
    // 目录扫描( 获取所有的存在的 Context 添加到 contextList 中 )（有：dictionary、object1、object2 应用）
    private static void scanContexts() {
        // 获取webapps文件夹
        File webappsRoot = new File(WEBAPPS_BASE);
        // 获取webapps文件夹下所有文件（每个 files[i] 都是一个项目）
        File[] files = webappsRoot.listFiles();
        // 如果为null，则抛出异常
        if (files == null) {
            throw new RuntimeException();
        }

        for (File file : files) {
            // 如果这个file不是目录，那么直接跳过
            if (!file.isDirectory()) {
                continue;
            }
            // 执行到这里说明是一个web应用项目

            // 获取当前应用(Context)名
            String contextName = file.getName();
            System.out.println(contextName);
            Context context = new Context(contextName);
            contextList.add(context);
        }
    }

    // 解析 web.Conf 文件
    private static void parseContextConf() {
        for (Context context : contextList) {
            // 根据 Context 获取项目名称，调用 ConfigReader.read(名称) 获取读取的 Config 类，并且注入给 Context 对象
            context.setConfig(ConfigReader.reader(context.getName()));
        }
    }

    // 加载 Servlet Class 类
    private static void loadServletClasses() throws ClassNotFoundException {
        for (Context context : contextList) {
            context.loadServletClasses();
        }
    }

    // 实例化 Servlet 类
    private static void instantiateServletObject() throws InstantiationException, IllegalAccessException {
        for (Context context : contextList) {
            context.instantiateServletObject();
        }
    }

    // 初始化 Servlet 类
    private static void initializeServletObject() throws ServletException {
        defaultServlet.init();
        notFoundServlet.init();
        for (Context context : contextList) {
            context.initializeServletObject();
        }
    }

    // 2. 处理服务器逻辑
    private static void startServer() throws IOException {
        // 多线程处理请求
        ExecutorService threadPool = Executors.newFixedThreadPool(10);

        ServerSocket serverSocket = new ServerSocket(8080);

        // 每次循环处理一次请求
        // 死循环，表示无限监听8080端口有没有请求，并处理
        while (true) {
            // 开启 Socket 端口，表示端口处于等待监听状态
            Socket socket = serverSocket.accept();
            // 调用多线程去处理任务（单次请求响应逻辑核心流程在 RequestResponseTask 中处理）
            Runnable task = new RequestResponseTask(socket);
            // 线程池分配一个线程去处理任务
            threadPool.execute(task);
        }
    }

    // 3. 销毁 Servlet
    private static void destroyServlet() {
        defaultServlet.destroy();
        notFoundServlet.destroy();
        for (Context context : contextList) {
            context.destroyServlet();
        }
    }



    @Test
    public void test() throws ClassNotFoundException, InstantiationException, ServletException, IllegalAccessException {
        initServer();
        System.out.println("===============");
        for (Context context : contextList) {
            for (Servlet servlet : context.servletList) {
                System.out.println(servlet);
            }
        }
    }

}
