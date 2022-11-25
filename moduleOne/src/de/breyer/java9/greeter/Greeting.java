package de.breyer.java9.greeter;

import de.breyer.java9.timer.Timer;

public class Greeting {

    public static void main(String[] args) {
        System.out.println("Greetings!");
        Timer timer = new Timer();
        System.out.println("It is " + timer.getTime());
    }

}
