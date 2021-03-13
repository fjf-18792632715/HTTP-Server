package com.fjf.反射Demo;

public class Demo{
    public static void main(String[] args) throws ClassNotFoundException {
        String className = "com.fjf.webapps.dictionary.TranslateServlet";

        Class<?> aClass = Class.forName(className);

        Class<?> aClass1 = Demo.class.getClassLoader().loadClass(className);




    }
}
