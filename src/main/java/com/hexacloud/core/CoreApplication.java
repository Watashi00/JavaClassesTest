package com.hexacloud.core;

import java.io.Console;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.hexacloud.core.annotations.MenuMethod;

public class CoreApplication{
    private Map<String, Command> map = new HashMap<>();
    private String generatedMenu = null;
    private String menu = null;
    private Map<String, menuObject> mapClasses;
    public static void init() throws IllegalAccessException, InvocationTargetException, InstantiationException, IllegalArgumentException, NoSuchMethodException, SecurityException {
        CoreApplication core = new CoreApplication();
        core.mapClasses = MapClasses.menuClasses;
        core.menu = core.classesMenu(core.mapClasses);
        core.menu();
    }

    void menu() throws IllegalAccessException, InvocationTargetException, InstantiationException, IllegalArgumentException, NoSuchMethodException, SecurityException {
        Console console = System.console();
        String rl = "";
        while(!rl.equals("break")) {
            System.out.println(menu);
            rl = console.readLine();
            if(menu.startsWith("MENU")) {
                if(mapClasses.containsKey(rl)) {
                    var v = mapClasses.get(rl);
                    var object = v.object();
                    menu = mountingInternMenu(object);
                }
            } else {
                if(map.containsKey(rl)) {
                    System.out.println("CALLING METHOD: " + map.get(rl).method().getName() + System.lineSeparator());
                    map.get(rl).method().invoke(map.get(rl).target(), null);
                    System.out.println(System.lineSeparator() + "END");

                }
                if(rl.equals("left")) {
                    menu = classesMenu(mapClasses);
                }
                
            }
        }
    }

    private Method[] getMethodsClass(Object object) {
       return object.getClass().getDeclaredMethods();
    }

    private String mountingInternMenu(Class<?> clazz) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        var instance = clazz.getDeclaredConstructor().newInstance();
        map.clear();
        StringBuilder string = new StringBuilder();
        Method[] methods = getMethodsClass(instance);
        int i = 1;
        for(Method m : methods) {
            if(m.isAnnotationPresent(MenuMethod.class)){
                String description = m.getAnnotation(MenuMethod.class).desc().isBlank() ? "" : " Description: " +m.getAnnotation(MenuMethod.class).desc();
                string.append("index: ").append(i).append("->").append(m.getName()).append(description).append(System.lineSeparator());
                map.put(String.valueOf(i), new Command(instance, m));
                i++;
            }
        }
        return string.append("left: return to menu").append(System.lineSeparator()).append("break: close application").append(" Finded: ").append(i - 1).append(" Method's").toString();
    }

    private String classesMenu(Map<String, menuObject> menuMap) {
        if(menuMap.isEmpty()) {
            MapClasses.getMenu();
            menuMap = MapClasses.menuClasses;
        }
        if(!(generatedMenu == null)){
            return generatedMenu;
        }
        StringBuilder string = new StringBuilder().append("MENU").append(System.lineSeparator());
        
        for(Map.Entry<String, menuObject> entry : menuMap.entrySet()) {
            string.append(entry.getKey()).append(": ->").append(entry.getValue().name()).append(" Description: ").append(entry.getValue().description()).append(System.lineSeparator());
        }
        
        return string.toString();
    }

}

record Command(Object target, Method method) {}
