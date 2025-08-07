package com.back;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        App app = new App(sc);
        app.run();
        sc.close();
    }
}