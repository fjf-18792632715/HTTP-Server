package com.fjf.Tomcat;

import com.fjf.Tomcat.utils.HttpRequestParser;
import com.fjf.Tomcat.http.Response;
import com.fjf.Tomcat.pojo.Context;
import com.fjf.Tomcat.utils.SessionUtil;
import com.fjf.standard.Servlet;
import com.fjf.standard.http.Cookie;
import com.fjf.standard.http.HttpServletRequest;
import com.fjf.standard.http.HttpSession;

import java.io.*;
import java.lang.reflect.Field;
import java.net.Socket;
import java.util.Map;

public class RequestResponseTask implements Runnable {
    // 单例对象，整个流程中只有一个，在初始化的时候就已经存在
    private static final HttpRequestParser parser = new HttpRequestParser();
    private final Socket socket;

    public RequestResponseTask(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            // 1. 读取、解析并得到 Request 请求对象
            System.out.println("得到 request");
            HttpServletRequest request = parser.parse(socket.getInputStream());

            // 2. 实例化一个 Response 响应对象
            System.out.println("得到 response");
            Response response = new Response();

            // 3. 根据 request.getContextPath() 找到哪个 Context（webapp）进行处理
            System.out.println("得到 context");
            Context context = null;
            String contextPath = request.getContextPath();
            for (Context contextTemp : HttpServer.contextList) {
                System.out.println(contextTemp.getName());
                if (contextTemp.getName().equals(contextPath)) {
                    context = contextTemp;
                    break;
                }
            }

            // 4. 根据 request.getServletPath() 找到 Context 中的哪个 HttpServlet 进行处理
            System.out.println("得到 servlet");
            Servlet servlet = null;
            if (context == null) {
                // 说明没找到项目，则 跳转到 NotFoundServlet
                servlet = HttpServer.notFoundServlet;
            } else {
                // 找到项目了，开始根据 servletPath 寻找 Servlet
                String servletName = context.getConfig().getUrlToServletName().get(request.getServletPath());
                String servletClassName = context.getConfig().getServletNameToServletClassPath().get(servletName);
                for (Servlet servletTemp : context.servletList) {
                    if (servletTemp.getClass().getCanonicalName().equals(servletClassName)) {
                        servlet = servletTemp;
                    }
                }
            }
            if (servlet == null) {
                // 如果没找到 servlet，说明有两种情况：1. 定位的是静态资源 2. 确实没有这个servlet    --->   需要跳转到 DefaultServlet
                servlet = HttpServer.defaultServlet;
            }

            // 5. 调用 servlet.service(request, response) 交给业务处理
            System.out.println("调用 service");
            servlet.service(request, response);


            // 6. 根据 response 对象中的数据，得到发送 HTTP 响应
            System.out.println("返回响应");
            sendResponse(request, response, socket.getOutputStream());

            socket.close();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    // 发送 Response 对象响应给浏览器
    private void sendResponse(HttpServletRequest request, Response response, OutputStream outputStream) throws IOException, NoSuchFieldException, IllegalAccessException {

        // 保存 Session： 1. 创建一个 Cookie（保存的SessionID） 加入 Response 响应给浏览器。 2. 保存 Session 文件到本地
        final HttpSession session = request.getSession();
        Field field1 = session.getClass().getDeclaredField("sessionID");
        field1.setAccessible(true);
        String sessionID = (String) field1.get(session);
        response.addCookie(new Cookie("session-id", sessionID));
        SessionUtil.saveSessionData(session);

        // 这个 printWriter 是根据 socket 获取的输出流
        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(outputStream));
        for (Cookie cookie : response.cookieList) {
            response.setHeader("Set-Cookie", String.format("%s=%s", cookie.getName(), cookie.getValue()));
        }

        // 响应行
        printWriter.printf("Http/1.0 %d\r\n", response.getStatus());
        // 响应头
        for (Map.Entry<String, String> entry : response.getHeaders().entrySet()) {
            String name = entry.getKey();
            String value = entry.getValue();
            printWriter.printf("%s: %s\r\n", name, value);
        }
        // 必须要有的，响应头和响应体之间有一行空行
        printWriter.printf("\r\n");

        // 响应体
        response.bodyPrintWriter.flush(); // 从缓存中将内容刷新到响应体流中
        response.bodyOutputStream.flush();  // 从缓存中将内容刷新到响应体流中
        printWriter.flush();  // 从缓存中将内容刷新到 socket 的流中

        // 将响应体流中的数据输出到一个 byte 数组中
        byte[] byteArray = response.bodyOutputStream.toByteArray();
        // 将 byte 数组中的数据写入到 socket 的流中
        outputStream.write(byteArray);
        outputStream.flush();


         /**
          * 这样在 socket 的流中的顺序就是：
          *     Http/1.0 status \r\n
          *     多个响应头内容组成的键值对
          *     \r\n
          *     响应体的流
         */
    }
}
