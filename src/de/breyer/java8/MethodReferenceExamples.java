package de.breyer.java8;

public class MethodReferenceExamples {

    public static Integer staticAdd(Integer param1, Integer param2) {
        return Integer.sum(param1, param2);
    }

    public Integer instanceAdd(Integer param1, Integer param2) {
        return Integer.sum(param1, param2);
    }
}
