package de.breyer.java9;

public class ExampleEvent {

    private final String name;

    public ExampleEvent(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
