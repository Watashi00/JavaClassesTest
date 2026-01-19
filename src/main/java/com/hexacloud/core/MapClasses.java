package com.hexacloud.core;

import java.util.HashMap;
import java.util.Map;

import com.hexacloud.core.annotations.Menu;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;

public class MapClasses {
    public static Map<String, menuObject> menuClasses = new HashMap<>();
    static String pkg = "com.hexacloud";

    public static void getMenu() {
        try (ScanResult scanResult =
                new ClassGraph()
                .enableClassInfo()
                .enableAnnotationInfo()
                .enableMethodInfo()
                .acceptPackages(pkg)
                .scan()
            
        ){ 
            int i = 1;
            for(ClassInfo router : scanResult.getClassesWithAnnotation(Menu.class.getName())) {
                Class<?> clazz = router.loadClass();
                String description = clazz.getAnnotation(Menu.class).desc();
                menuClasses.put(String.valueOf(i), new menuObject(clazz.getName(), clazz, description));
                i++;
            }
        } 
    }
}


record menuObject(String name, Class<?> object, String description) {}
