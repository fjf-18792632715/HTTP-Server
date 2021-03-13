package com.fjf.Tomcat.utils;

import com.fjf.Tomcat.pojo.Config;
import com.fjf.Tomcat.HttpServer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ConfigReader {
    public static Config reader(String contextName){
        Config config = new Config(new HashMap<>(), new HashMap<>());

        // 对每一个 web.conf 文件进行 读取 + 解析
        String fileNamePath = String.format("%s/%s/WEB-INF/web.conf", HttpServer.WEBAPPS_BASE, contextName);

        // 使用有限状态机进行文件解析
        String state = "start"; // "servlets"、"mappings"
        // 进行文本文件的读取
        try (InputStream inputStream = new FileInputStream(fileNamePath)){
            Scanner sc = new Scanner(inputStream, "utf-8");
            while (sc.hasNext()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                // 模拟有限状态机
                switch (state) {
                    case "start":
                        if (line.equals("servlets:")) {
                            state = "servlets";
                        }
                        break;
                    case "servlets":
                        if (line.equals("servlet-mappings:")) {
                            state = "mappings";
                        } else {
                            // TODO 解析 servlets 内容
                            String[] split = line.split("=");
                            String servletName = split[0].trim();
                            String servletClassPath = split[1].trim();
                            config.getServletNameToServletClassPath().put(servletName, servletClassPath);
                        }
                        break;
                    case "mappings":
                        if (line.equals("servlets:")){
                            state = "servlets";
                        } else {
                            // TODO 解析 servlet-mappings 内容
                            String[] split = line.split("=");
                            String urlPattern = split[0].trim();
                            String servletName = split[1].trim();
                            Map<String, String> map = config.getUrlToServletName();
                            if (!map.containsKey(urlPattern)) {
                                map.put(urlPattern, servletName);
                            }
                        }
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 进行配置文件的解析工作
        System.out.println(config.getServletNameToServletClassPath());
        System.out.println(config.getUrlToServletName());

        return config;
    }
}
