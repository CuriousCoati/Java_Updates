package de.breyer.java17;

import java.util.random.RandomGeneratorFactory;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Starter {

    public static void main(String[] args) {
        Starter starter = new Starter();

        starter.randomGenerator();
        starter.sealedClasses();

        // Always-Strict Floating-Point were restored

        // Apple Metal API is used in Swing, as OpenGL API was deprecated from Apple

        // JDK can run on AArch64 macOS platforms

        // applet is deprecated

        // RMI activation API was removed

        // GraalVM AOT and JIT compiler was removed from JDK

        // security manager is marked for removal

        // context-specific and dynamically selected deserialization filters were added
    }

    private void sealedClasses() {
        System.out.println("### sealed classes ###");

        // with sealed classes it is possible to restrict the number of implementing or extending class
        // sealed keyword must be added at the class or interface and permits keyword + implementing/extending classes must be listed
        // the extending/implementing classes must be final, non-sealed or sealed

        // see SealedInterface/SealedClass

        System.out.println();
    }

    private void randomGenerator() {
        System.out.println("### random generator ###");

        // new pseudo random generator interfaces and implementations were added
        // algorithm can be changed easier and stream based programming is better supported
        IntStream randomInts = RandomGeneratorFactory.of("Xoshiro256PlusPlus").create().ints(0,10);
        System.out.println("random ints: " + randomInts.limit(10).mapToObj(String::valueOf).collect(Collectors.joining(",")));

        System.out.println();
    }

}
