package de.breyer.java9;

public interface ExampleInterface {

    private static void privateStaticPrint(String output) {
        System.out.println("privateStaticPrint: " + output);
    }

    private void privatePrint(String output) {
        System.out.println("privatePrint: " + output);
    }

    default void defaultMethod() {
        privateStaticPrint("called from default Method");
        privatePrint("called from default Method");
    }

    void doStuff();
}
