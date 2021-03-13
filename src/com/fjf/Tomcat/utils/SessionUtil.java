package com.fjf.Tomcat.utils;

import com.fjf.Tomcat.http.HttpSessionImpl;
import com.fjf.standard.http.HttpSession;
import org.junit.Test;

import java.io.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class SessionUtil {
    public static final String BASE_SESSION = "E:\\IDEA2020\\java-object\\HTTP-Server\\src\\com\\fjf\\Tomcat\\session";

    public static Map<String, Object> loadSession(String sessionID){
        File sessionRoot = new File(BASE_SESSION);
        HashMap<String, Object> map = new HashMap<>();
        String fileName = sessionID + ".sid";

        File[] files = sessionRoot.listFiles();
        if (files == null) {
            throw new RuntimeException();
        }
        for (File file : files) {
//            System.out.println(file.getName());
            if (file.getName().equals(fileName)) {
                // 找到 sessionId 对应的序列化文件（读取持久化文件）
                String sessionPath = BASE_SESSION + "/" + fileName;
//                System.out.println(sessionPath);
                try (InputStream inputStream = new FileInputStream(sessionPath)){
                    ObjectInputStream reader = new ObjectInputStream(inputStream);
                    map = (HashMap<String, Object>) reader.readObject();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return map;
    }

    public static void saveSessionData(HttpSession session) throws IllegalAccessException, NoSuchFieldException {
        Field field = session.getClass().getDeclaredField("sessionData");
        Field field1 = session.getClass().getDeclaredField("sessionID");
        field.setAccessible(true);
        field1.setAccessible(true);
        // 获取 sessionData、sessionID
        Map<String, Object> sessionData = (Map<String, Object>) field.get(session);
        String sessionID = (String) field1.get(session);

        // 如果 sessionData 没有内容就不用持久化到本地了
        if (sessionData.isEmpty()) {
            return;
        }

/*        sessionID = "test1"; // 测试所用，应该注释
        System.out.println("进入 saveSessionData ");
        System.out.println(sessionData);
        System.out.println(sessionID);*/

        // 持久化 sessionData
        String savePath = BASE_SESSION + "/" + sessionID + ".sid";
        try (OutputStream outputStream = new FileOutputStream(savePath)){
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(sessionData);
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadSession(){
        String sessionId = "test1";
        Map<String, Object> map = loadSession(sessionId);
        System.out.println(map);
    }

    @Test
    public void testSaveSessionData() throws NoSuchFieldException, IllegalAccessException {
        saveSessionData(new HttpSessionImpl("test"));
    }
}
