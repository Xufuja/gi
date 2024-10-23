package dev.xfj;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ClassGenerator classGenerator = new ClassGenerator("C:\\Dev\\StarRailData\\ExcelOutput\\", "C:\\Dev\\hsr\\hsr-core\\src\\generated\\");
        classGenerator.createClasses();
        System.out.println("Class generation done!");
    }
}