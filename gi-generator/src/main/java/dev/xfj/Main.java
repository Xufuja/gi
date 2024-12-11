package dev.xfj;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        new ClassGenerator("C:\\Dev\\AnimeGameData\\ExcelBinOutput\\", "C:\\Dev\\gi\\gi-core\\src\\generated\\").createClasses();
        new ClassGenerator("C:\\Dev\\AnimeGameData\\BinOutput\\Avatar\\", "C:\\Dev\\gi\\gi-core\\src\\generated\\").createClasses(FileFilter.CHIORI_TEST, false);
        //new ClassGenerator("C:\\Dev\\AnimeGameData\\BinOutput\\Avatar\\", "C:\\Dev\\gi\\gi-core\\src\\generated\\").createClasses(FileFilter.MORE_THAN_1_UNDERSCORE, false);
        //new ClassGenerator("C:\\Dev\\AnimeGameData\\BinOutput\\Ability\\Temp\\AvatarAbilities\\", "C:\\Dev\\gi\\gi-core\\src\\generated\\").createClasses(FileFilter.NONE, false);
        System.out.println("Class generation done!");
    }
}