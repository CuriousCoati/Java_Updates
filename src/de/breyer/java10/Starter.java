package de.breyer.java10;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

public class Starter {

//    private var test = "Test"; -> compile error

    public static void main(String[] args) {
        Starter starter = new Starter();

        starter.localVariableTypeInference();
        starter.unmodifiableCollections();
        starter.optionals();

        // the full GC algorithm was changed to a parallel mark-sweep-compact algorithm and effectively reduces the stop time

        // class-data sharing (CDS) was extended with AppCDS, with this app/platform/custom class loader can load archived classes
        // therefore application classes can now use this feature

        // with java 10 graal can be used as an expermital JIT compiler

        // JVWs are now aware of being run in a container
        // can be disabled with -XX:-UseContainerSupport
        // furthermore more JVW moptions were added to control resources
        // -XX:ActiveProcessorCount, -XX:InitialRAMPercentage, -XX:MaxRAMPercentage, -XX:MinRAMPercentage

        // Oracle open-sourced the root certificates, so TLS will work out of the box with OpenJDK
    }

    private void optionals() {
        System.out.println("### optionals ###");

        try {
            List<Integer> intList = List.of(1,2,3,4,5);
            Integer result = intList.stream().filter(i -> i < 0).findFirst().orElseThrow();

            System.out.println("[error should not reach this] found negativ int: " + result);

        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }

        System.out.println();
    }

    private void unmodifiableCollections() {
        System.out.println("### unmodifiable collections ###");

        // List, Map and Set have a new method copyOf to create an unmodifiable List/Map/Set of a collection or map
        Map<Integer, String> exMap = new HashMap<>();
        exMap.put(1, "one");
        exMap.put(2, "two");
        exMap.put(3, "three");
        exMap.put(4, "four");

        System.out.println("List: " + List.copyOf(exMap.values()));
        System.out.println("Set: " + Set.copyOf(exMap.values()));
        System.out.println("Map: " + Map.copyOf(exMap));

        // furthermore toUnmodifiable* was added to Collectors
        System.out.println("Collectors toUnmodifiableList: " + exMap.values().stream().collect(Collectors.toUnmodifiableList()));
        System.out.println("Collectors toUnmodifiableSet: " + exMap.values().stream().collect(Collectors.toUnmodifiableSet()));

        System.out.println();
    }

    private void localVariableTypeInference() {
        System.out.println("### local variable type inference ###");

        // with java 10 the type of local variables can be unspecific
        String message = "This is a String";
        var messageTwo = "This is a unspecified string"; // compiler infers the type from the initializer

        // at runtime messageTwo is still a String and can't be changed
        // it's just vor shorter code

        System.out.println("message one: " + message);
        System.out.println("message two: " + messageTwo);

        // var must be used with an initializer and is only for local variables
//        var test;         -> compile error
//        var test = null;  -> compile error

        // var is not allowed for lambdas
//        var lambda = (String s) -> s.length() > 10; -> compile error

        // array initializers don't work, they need explicit typing
//        var arr = {1, 2, 3}; -> compile error
        var arr = new int[]{1, 2, 3};

        // be careful, it's easy to ruin readability
        var result = doStuff();         // method must be read to find type
        var exampleList = new ArrayList<>();    // example is a ArrayList<Object>
        var impl = new ExampleInterface() {};
//        impl = new ExampleInterface() {}; -> compile error, because impl is not of the interface type, it's typed with the anonymous class

        System.out.println();
    }

    private boolean doStuff() {
        return true;
    }

}
