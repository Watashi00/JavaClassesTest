package com.hexacloud.core;

import java.io.Console;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.hexacloud.core.annotations.CustomAnnotationsAndComportaments;

public class CoreApplication{
    private Map<String, Command> map = new HashMap<>();
    private Map<String, Object> classMap = new HashMap<>();
    public static void init() throws IllegalAccessException, InvocationTargetException {
        Console console = System.console();
        
        CoreApplication core = new CoreApplication();
        String response = "";
        String menu = """
                MENU
                1: customAnnotations
                break: close application
                """;
        while(!response.equals("break")){
            core.menu(menu);
            response = console.readLine();
            switch (response) {
                case "1" -> core.customAnnotationsAndComportaments();
                case "break" -> System.out.println("Closing application");
                default -> core.menu(menu);
            }
        }
    }

    void menu(String menu) throws IllegalAccessException, InvocationTargetException {
        if(menu.startsWith("MENU")) {
            System.out.println(menu);
        }else {
            Console console = System.console();
            String response = "";
            System.out.println(menu);
            while(!response.equals("break")) {
                response = console.readLine();
                if(map.containsKey(response)) {
                    map.get(response).method().invoke(map.get(response).target(), null);
                };
            }
        }
    }

    void customAnnotationsAndComportaments() throws IllegalAccessException, InvocationTargetException {
        CustomAnnotationsAndComportaments a = new CustomAnnotationsAndComportaments();
        menu(mountingInternMenu(a));
    }

    private Method[] getMethodsClass(Object object) {
       return object.getClass().getDeclaredMethods();
    }

    private String mountingInternMenu(Object obj) {
        map.clear();
        StringBuilder string = new StringBuilder();
        Method[] methods = getMethodsClass(obj);
        int i = 1;
        for(Method m : methods) {
            string.append("index: ").append(i).append("->").append(m.getName()).append(System.lineSeparator());
            map.put(String.valueOf(i), new Command(obj, m));
            i++;
        }
        return string.toString();
    }

}

record Command(Object target, Method method) {}
