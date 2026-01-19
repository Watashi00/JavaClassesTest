package com.hexacloud.core.annotations;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

@Menu(desc = "custom annotation")
public class CustomAnnotationsAndComportaments {
    @MenuMethod
    public void myCustomMethodWithAnnotation(){
        System.out.println("Method invoke");
    }
    
    @MenuMethod
    public void myCustomMethod() {
        System.out.println("Second method");
    }

    @MenuMethod
    public void test() {
        Process process = new Process();
        try {
            process.getAnnotations(this);   
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}


class Process {
    public void getAnnotations(Object obj) throws IllegalAccessException, InvocationTargetException, IOException {
        
        var methods = obj.getClass().getDeclaredMethods();
        for(Method m : methods) {
           if(m.isAnnotationPresent(PrintValue.class)) {
                Properties props = new Properties();
                props.load(getClass().getClassLoader()
                .getResourceAsStream("application.properties"));  
                boolean debug = Boolean.getBoolean(props.getProperty("debug")); 
                if(debug) {
                    var annotation = m.getAnnotation(PrintValue.class);
                    System.out.println(annotation.value());
                }
                m.invoke(obj);
           }
        }
    }
}


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface PrintValue {
    String value();
}
