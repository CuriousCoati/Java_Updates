package de.breyer.java8;

import java.util.function.LongBinaryOperator;

public class FunctionalInterfaceExample {

    private LongBinaryOperator addFunction;

    public LongBinaryOperator run() {
        // add function as anonymous class
        addFunction = new LongBinaryOperator() {
            @Override
            public long applyAsLong(long left, long right) {
                return left + right;
            }
        };

        // add function as lambda
        addFunction = (left, right) -> left + right;

        // add function as method reference
        addFunction = Long::sum;

        // now this functional interface impl can be passed and executed anywhere
        System.out.println("add function as functional interface in FunctionalInterfaceExample: "  + addFunction.applyAsLong(1, 2));
        return addFunction;
    }
}
