package de.breyer.java11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Starter {

    public static void main(String[] args) {
        Starter starter = new Starter();

        starter.stringClass();
        starter.filesClass();
        starter.collections();
        starter.notPredicate();
        starter.varInLambdas();

        // the new http client (Java 9) is now default

        // java 11 introduces nest based access control
        // this fixed a problem with reflection access to private members in nested classes

        // with java 11 the java binary can run a java class directly

        // java 11 contains multiple performance improvements
        // - dynamic class-file constants
        // - improved Aarch64 intrinsics
        // - no-op garbage collector

        // java flight recorder is now part of open jdk and can be used for diagnostics and profiling

        // Java EE, CORBA and JavaFx are now removed form core java

        // JMC is no longer part of JDK and can be downloaded separately
    }

    private void varInLambdas() {
        System.out.println("### not predicate ###");

        // var can now be used in lambdas
        List<String> sampleList = List.of("one", "two", "three");
        String uppercased = sampleList.stream()
                .map((var x) -> x.toUpperCase())
                .collect(Collectors.joining(", "));

        System.out.println("uppercased: " + uppercased);

        System.out.println();
    }

    private void notPredicate() {
        System.out.println("### not predicate ###");

        // a not predicate was added to negate predicates
        String largeText = "Zeile 1 \n Zeile 2 \n \n Zeile 4";
        List<String> lines = largeText.lines()
                .filter(Predicate.not(String::isBlank)) // lambda see stringClass can be replaced with a method reference
                .map(String::strip)
                .collect(Collectors.toList());
        System.out.println("lines: " + lines);

        System.out.println();
    }

    private void collections() {
        System.out.println("### collections ###");

        List<String> stringList = List.of("one", "two", "three");
        // collections can now easily be converted to arrays
        String[] stringArray = stringList.toArray(String[]::new);
        System.out.println("String Array: " + stringArray);

        System.out.println();
    }

    private void filesClass() {
        System.out.println("### files class ###");

        try {
            Path workingDir = Paths.get("");
            Path tmpFile = Files.createTempFile(workingDir, "test", ".txt");

            // files class was extended to easily write and read strings from/to files
            Files.writeString(tmpFile, "Zeile 1 \n Zeile 2");

            String content = Files.readString(tmpFile);
            System.out.println("file content: " + content);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println();
    }

    private void stringClass() {
        System.out.println("### string class ###");

        // there are a few new methods on the string class
        // lines() separates the lines at line terminators
        String largeText = "Zeile 1 \n Zeile 2 \n \n Zeile 4";
        List<String> lines = largeText.lines()
                // isBlank() checks if a string is empty or consist only of white space characters
                .filter(line -> !line.isBlank())
                // strip() removes all leading and trailing white spaces (just like trim, but unicode-aware)
                .map(String::strip)
                .collect(Collectors.toList());

        System.out.println("lines: " + lines);

        // with repeat() you get a concatenated string with the original content repeated x times
        String pseudoText = "Lorem ipsum dolor sit amet.\n".repeat(5);
        System.out.println("repeat: " + pseudoText);

        System.out.println();
    }

}
