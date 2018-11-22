package com.sherlocky.interview.javase.singleton.test;

import com.sherlocky.interview.javase.singleton.InnerClassSingleton;
import java.io.*;

public class SerializeSaveAndReadTest {
    public static void main(String[] args) {
        try {
            InnerClassSingleton instance = InnerClassSingleton.getInstance();
            FileOutputStream fosRef = new FileOutputStream(new File("InnerClassSingleton.txt"));
            ObjectOutputStream oosRef = new ObjectOutputStream(fosRef);
            oosRef.writeObject(instance);
            oosRef.close();
            fosRef.close();
            System.out.println("序列化前：" + instance.hashCode());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileInputStream fisRef = new FileInputStream(new File("InnerClassSingleton.txt"));
            ObjectInputStream iosRef = new ObjectInputStream(fisRef);
            InnerClassSingleton myObject = (InnerClassSingleton) iosRef.readObject();
            iosRef.close();
            fisRef.close();
            System.out.println("序列化后：" + myObject.hashCode());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
