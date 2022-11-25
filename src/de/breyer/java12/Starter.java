package de.breyer.java12;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.text.NumberFormat.Style;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class Starter {

    public static void main(String[] args) {
        Starter starter = new Starter();

        starter.stringClass();
        starter.fileClass();
        starter.teeingCollector();
        starter.compactNumberFormatter();

        // JDK has now microbenchmark tests, which JDK developers can use

        // CDS changes from java 10 are now enabled by default
        // -Xshare:off can be used to turn it off -> startup time of a program may be slower
    }

    private void compactNumberFormatter() {
        System.out.println("### compact number formatter ###");

        // compact format formatter can be used to shorten numbers based on a local
        // e.g. 1000 in us is 1k, or if witten out 1 thousand
        long number = 11345;

        NumberFormat formatter = NumberFormat.getCompactNumberInstance(Locale.US, Style.LONG);
        System.out.println("long format: " + formatter.format(number));

        formatter = NumberFormat.getCompactNumberInstance(Locale.US, Style.SHORT);
        formatter.setMaximumFractionDigits(2);
        System.out.println("short format: " + formatter.format(number));

        System.out.println();
    }

    private void teeingCollector() {
        System.out.println("### teeing collector ###");

        // the teeing collector can be used to perform two collectors on the list
        // both results must be merged in a BiFunction
        List<Integer> intList = List.of(7891,5164,12,78123,-4781,-761,184789);
        int[] minAndMax = intList.stream().collect(
                Collectors.teeing(
                        Collectors.minBy(Comparator.comparingInt(value -> value)),
                        Collectors.maxBy(Comparator.comparingInt(value -> value)),
                        (r1, r2) -> new int[] { r1.orElse(0), r2.orElse(0) } ));

        System.out.println("list: " + intList);
        System.out.println("min:" + minAndMax[0] + " - max: " + minAndMax[1]);


        System.out.println();
    }

    private void fileClass() {
        System.out.println("### files class ###");

        try {
            // with the new mismatch method of the files class, two files can be compared
            // the position of the first mismatch will be returned
            Path pathOne = Files.createTempFile("one", ".txt");
            Path pathTwo = Files.createTempFile("two", ".txt");
            Path pathThree = Files.createTempFile("three", ".txt");

            Files.writeString(pathOne, "Lorem ipsum dolor");
            Files.writeString(pathTwo, "Lorem ipsum dolor sit amet");
            Files.writeString(pathThree, "Lorem ipsum dolor");

            System.out.println("mismatch between 1 and 2? [yes]: " + Files.mismatch(pathOne, pathTwo));
            System.out.println("mismatch between 1 and 3? [no]: " + Files.mismatch(pathOne, pathThree));

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println();
    }

    private void stringClass() {
        System.out.println("### string class ###");

        // there are a few new methods on the string class
        // indent() adds or removes leading white spaces on every line
        String largeText = "Zeile 1\n Zeile 2\n\n Zeile 4";
        System.out.println("no indent: " + largeText.indent(-1)); // remove
        System.out.println("indent of 4: " + largeText.indent(4)); // add

        // transform can be used to transform a string, a single argument function (functional interface) must be provided
        String origin = "Hello World!";
        String reversed = origin.transform(text -> {
            String[] split = text.split(" ");
            Collections.reverse(Arrays.asList(split));
            return String.join(" ", split);
        });
        System.out.println("reversed words: " + reversed);

        System.out.println();
    }

}
