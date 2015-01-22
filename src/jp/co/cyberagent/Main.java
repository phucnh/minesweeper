package jp.co.cyberagent;

import jp.co.cyberagent.logic.ConsoleGameController;

public class Main {

    public static void main(String[] args) {

        // run application
        try {
            (new ConsoleGameController()).run();
        } catch (Exception e) {
            e.printStackTrace();

            // exit game
            System.exit(0);
        }

    }
}
