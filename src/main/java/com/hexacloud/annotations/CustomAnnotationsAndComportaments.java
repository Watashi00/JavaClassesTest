package com.hexacloud.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CustomAnnotationsAndComportaments {
    
    @PrintValue("First method")
    public void myCustomMethodWithAnnotation(){
        System.out.println("Method invoke");
    }

    public void myCustomMethod() {
        System.out.println("Second method");
    }

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
        }
    }
}


class Process {
    public void getAnnotations(Object obj) throws IllegalAccessException, InvocationTargetException {
        var methods = obj.getClass().getDeclaredMethods();
        for(Method m : methods) {
           if(m.isAnnotationPresent(PrintValue.class)) {
                var annotation = m.getAnnotation(PrintValue.class);
                System.out.println(annotation.value());
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
