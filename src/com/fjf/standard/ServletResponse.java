package com.fjf.standard;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public interface ServletResponse {

    OutputStream getOutputStream();
    PrintWriter getWriter() throws IOException;
    // 设置响应类型
    void setContentType(String type);
}
