package com.hexacloud;

import java.lang.reflect.InvocationTargetException;

import com.hexacloud.core.CoreApplication;

public class Main {
    public static void main(String[] args) {
        try {
            CoreApplication.init();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}