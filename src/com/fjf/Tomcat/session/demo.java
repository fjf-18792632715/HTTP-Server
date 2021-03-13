package com.fjf.Tomcat.session;

import com.fjf.Tomcat.utils.SessionUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class demo {

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", new ArrayList<String>());
        map.put("c", "asdasd");

        String fileName = SessionUtil.BASE_SESSION + "/test.sid";
        try (OutputStream outputStream = new FileOutputStream(fileName)){
            ObjectOutputStream writer = new ObjectOutputStream(outputStream);
            writer.writeObject(map);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
