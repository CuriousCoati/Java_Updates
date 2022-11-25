package de.breyer.java8;

import java.util.List;
import java.util.Optional;

public class OptionalExample {

    public static Optional<String> createEmptyOptional() {
        return Optional.empty();
    }

    public static Optional<String> createOptionalOfNonNullValue() {
        return Optional.of("NOT NULL");
    }

    public static Optional<String> createOptionalOfNullableValue(String value) {
        return Optional.ofNullable(value);
    }

    public static List<String> getStringList() {
        return null;
    }

    public static Optional<List<String>> getStringListOptional() {
        return Optional.empty();
    }

    public static Employee getEmployee() {
        return null;
    }

    public static Optional<Employee> getOptEmployee() {
        return Optional.empty();
    }
}
