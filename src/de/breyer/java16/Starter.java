package de.breyer.java16;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Starter {

    public static void main(String[] args) {
        Starter starter = new Starter();

        starter.defaultMethod();
        starter.dayPeriod();
        starter.stream();
        starter.patternMatching();
        starter.records();

        // JDK internals are now strongly encapsulated and –illegal-access=permit must be added to use them
    }

    private void records() {
        System.out.println("### records ###");

        // to create immutable data holding object, a lot of boilerplate code is needed
        // with records this boilerplate code can be omitted
        ImmutableCar carOne = new ImmutableCar("Golf", "VW");
        CarRecord carTwo = new CarRecord("Skyline", "Nissan");

        System.out.println("car one: " + carOne);
        System.out.println("car two: " + carTwo);

        System.out.println();
    }

    private void patternMatching() {
        System.out.println("### pattern matching ###");

        // with java 16 casting after instance of can be omitted, as a typed variable can be declared
        Object obj = "no cast needed";

        if (obj instanceof String text) {
            System.out.println(text);
        }

        System.out.println();
    }

    private void stream() {
        System.out.println("### stream ###");

        // stream api now has toList() method, to shorten common collect() constructs
        List<String> stringList = List.of("one", "two", "three");
        System.out.println("toList: " + stringList.stream().toList());

        System.out.println();
    }

    private void dayPeriod() {
        System.out.println("### day period ###");

        // DateTimeFormatter now support the B symbol as an alternative to am/pm format
        LocalTime time = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h B");
        System.out.println("h B: " + time.format(formatter));
        formatter = DateTimeFormatter.ofPattern("h BBBB");
        System.out.println("h BBBB: " + time.format(formatter));
        formatter = DateTimeFormatter.ofPattern("h BBBBB");
        System.out.println("h BBBBBB: " + time.format(formatter));

        System.out.println();
    }

    private void defaultMethod() {
        System.out.println("### default method ###");

        try {
            // the default method of an interface can now be called via a dynamic proxy via reflection
            Object proxy = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class<?>[] { ExampleInterface.class },
                    (prox, method, args) -> {
                        if (method.isDefault()) {
                            return InvocationHandler.invokeDefault(prox, method, args);
                        } else {
                            return null;
                        }
                    }
            );

            Method method = proxy.getClass().getMethod("greet");
            System.out.println(method.invoke(proxy));

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }

        System.out.println();
    }

}
