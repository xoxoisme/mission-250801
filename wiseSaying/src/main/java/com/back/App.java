package com.back;

import java.util.Scanner;

public class App {
    private Scanner sc;
    private final AppController appController;

    public App(Scanner sc) {
        this.sc = sc;
        this.appController = new AppController(sc);
    }

    public void run() {
        appController.loadData();
        System.out.println("== 명언 앱 ==");
        while (true) {
            System.out.print("명령) ");
            String cmd = sc.nextLine();
            if (cmd.equals("종료")) break ;
            appController.command(cmd);
        }
    }
}
