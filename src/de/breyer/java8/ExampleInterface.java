package de.breyer.java8;

public interface ExampleInterface {

// ### static methods in interfaces
    static String getMessage() {
        return "this message comes from a static method in an interface";
    }
// ### static methods in interfaces

// ### default methods in interfaces
    default int getMaxCapacity() {
        return 10;
    }
// ### default methods in interfaces
}
