package de.breyer.java8;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.LongBinaryOperator;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Starter {

    public static void main(String[] args) {
        Starter starter = new Starter();

        starter.staticMethodOnInterface();
        starter.defaultMethodOnInterface();
        starter.functionalInterfaces();
        starter.lambdas();
        starter.methodReferences();
        starter.optionals();
        starter.streams();
        starter.base64();
        starter.stringJoiner();
        starter.newDateTimeApi();
        starter.parallelArraySorting();
        starter.annotations();
    }

    private void staticMethodOnInterface() {
        System.out.println("### static method on interface ###");
        // of course static methods are not available on implementing classes
        System.out.println(ExampleInterface.getMessage());
        // SimpleExampleInterface.getMessage(); - ERROR
        System.out.println();
    }

    private void defaultMethodOnInterface() {
        System.out.println("### default method on interface ###");
        SimpleExampleInterface simple = new SimpleExampleInterface();
        AdvancedExampleInterface advanced = new AdvancedExampleInterface();

        System.out.println(simple.getMaxCapacity());
        System.out.println(advanced.getMaxCapacity());
        System.out.println();
    }

    private void printResult(int param1, int param2, BiFunction<Integer, Integer, Integer> term) {
        System.out.println("Result: " + term.apply(param1, param2));
    }

    private ExampleInterface createNewExampleInterface(Supplier<ExampleInterface> supplier) {
        ExampleInterface exampleInterface = supplier.get();
        System.out.println("created interface: " + exampleInterface.getClass().getSimpleName());
        return exampleInterface;
    }

    private void methodReferences() {
        System.out.println("### method references ###");
        // method references can be used to call functional interfaces instead of lambdas

        // lambda (IntelliJ warns as method reference can be used)
        printResult(3, 4, (a, b) -> a + b);

        // reference to static method
        printResult(10,9, MethodReferenceExamples::staticAdd);

        // reference to instance method
        MethodReferenceExamples methodReferenceExamples = new MethodReferenceExamples();
        printResult(6,2, methodReferenceExamples::instanceAdd);

        // reference to instance method of default type
        printResult(8,9, Integer::sum);

        // reference to a constructor
        createNewExampleInterface(SimpleExampleInterface::new);
        createNewExampleInterface(AdvancedExampleInterface::new);

        System.out.println();
    }

    private void printOptional(Optional<String> optional) {
        System.out.println(optional.orElse("empty"));
    }

    private void deepDiveOldWay() {
        Employee employee = OptionalExample.getEmployee();
        if (null != employee) {
            DriverLicense driverLicense = employee.getDriverLicense();
            if (null != driverLicense) {
                String state = driverLicense.getState();
                if (null != state) {
                    System.out.println(state);
                    return;
                }
            }
        }
        System.out.println("not specified");
    }

    private void deepDiveNewWayGetters() {
        Optional<Employee> employee = OptionalExample.getOptEmployee();
        System.out.println(employee.map(Employee::getDriverLicense).map(DriverLicense::getState).orElse("not specified"));
    }

    private void deepDiveNewWayOptGetters() {
        Optional<Employee> employee = OptionalExample.getOptEmployee();
        System.out.println(employee.flatMap(Employee::getOptDriverLicense).flatMap(DriverLicense::getOptState).orElse("not specified"));
    }

    private void changeNpeOldWay() throws NotSetException {
        String value = null;
        String result = "";
        try {
            result = value.substring(4);
        } catch (NullPointerException e) {
            throw new NotSetException();
        }
    }

    private void changeNpeNewWay() throws NotSetException {
        String value = null;
        String result = "";
        Optional<String> optString = Optional.ofNullable(value);
        result = optString.orElseThrow(NotSetException::new).substring(4);
    }

    private void optionals() {
        System.out.println("### optionals ###");

        // there are three ways to create an optional
        printOptional(OptionalExample.createEmptyOptional());
        printOptional(OptionalExample.createOptionalOfNonNullValue());
        printOptional(OptionalExample.createOptionalOfNullableValue(null));
        printOptional(OptionalExample.createOptionalOfNullableValue("value"));

        // example one: shorter/easier if not exists pattern
        // old way
        List<String> stringList = OptionalExample.getStringList();
        stringList = stringList != null ? stringList : new ArrayList<>();

        // new way
        stringList = OptionalExample.getStringListOptional().orElse(new ArrayList<>());

        // deep diving nullable object paths is way shorter/easier
        // old way
        deepDiveOldWay();
        // new way with simple getters uses map
        deepDiveNewWayGetters();
        // new way with optional getters uses flatMap
        deepDiveNewWayOptGetters();

        // change NPE to custom exception
        // old way
        try {
            changeNpeOldWay();
        } catch (NotSetException e) {
            System.out.println("old way: NotSetException caught");
        }

        // new way
        try {
            changeNpeNewWay();
        } catch (NotSetException e) {
            System.out.println("new way: NotSetException caught");
        }

        System.out.println();
    }

    private void base64() {
        System.out.println("### base64 ###");

        // base64 is now part of java
        String text = "BASE64 is now Part of default JAVA API";
        String encoded = Base64.getEncoder().encodeToString(text.getBytes());
        String decoded = new String(Base64.getDecoder().decode(encoded));
        System.out.println(text + " -> encoded -> " + encoded);
        System.out.println("decoded -> " + decoded);

        // padding can be skipped
        encoded = Base64.getEncoder().withoutPadding().encodeToString(text.getBytes());
        decoded = new String(Base64.getDecoder().decode(encoded));
        System.out.println(text + " -> encoded without padding -> " + encoded);
        System.out.println("decoded -> " + decoded);

        // urls should be encoded in a safe alphabet
        String url = "https://www.cologne-intelligence.de/team?tx_solr[q]=Breyer#search-result";
        encoded = Base64.getUrlEncoder().encodeToString(url.getBytes(StandardCharsets.UTF_8));
        decoded = new String(Base64.getUrlDecoder().decode(encoded));
        System.out.println(url + " -> url encoded -> " + encoded);
        System.out.println("url decoded -> " + decoded);

        // mime can also be encoded
        StringBuilder buffer = getMimeBuffer();
        String encodedMime = Base64.getMimeEncoder().encodeToString(buffer.toString().getBytes());
        String decodedMime = new String(Base64.getMimeDecoder().decode(encodedMime));
        System.out.println(buffer + " -> mime encoded -> " + encodedMime);
        System.out.println("mime decoded -> " + decodedMime);

        System.out.println();
    }

    private StringBuilder getMimeBuffer() {
        StringBuilder buffer = new StringBuilder();
        for (int count = 0; count < 10; ++count) {
            buffer.append(UUID.randomUUID());
        }
        return buffer;
    }

    private void stringJoiner() {
        System.out.println("### string joiner ###");

        // new StringJoiner can be used to join strings
        int[] ids = { 1578, 6425, 1235, 2578, 3215 };
        StringJoiner inJoiner = new StringJoiner(",", "(", ")");

        for (int id : ids) {
            inJoiner.add("" + id);
        }

        System.out.println("in clause: " + inJoiner);

        // empty value can be defined
        StringJoiner emptyJoiner = new StringJoiner(",");
        emptyJoiner.setEmptyValue("not defined");
        System.out.println("empty value: " + emptyJoiner);

        // StringJoiners can be merged, prefix and suffix of second joiner will not be used, but delimiter
        StringJoiner joiner1 = new StringJoiner(",", "(", ")");
        StringJoiner joiner2 = new StringJoiner(";", "[", "]");

        joiner1.add("one").add("two").add("three");
        joiner2.add("four").add("five").add("six");

        joiner1.merge(joiner2);

        System.out.println("merge: " + joiner1);

        System.out.println();
    }

    private void newDateTimeApi() {
        System.out.println("### new date time api ###");

        // localDate can be used for dates without time and timezone
        localDateStuff();
        // localTime can be used for time without date and timezone
        localTimeStuff();
        // localDateTime can be used for timestamps without timezone
        localDateTimeStuff();
        // ZonedDateTime can be used for timestamps with timezone
        ZonedDateTimeStuff();
        // all APIs are pretty similar

        // Period can be used to describe a quantity of time in days, month, years
        periodStuff();
        // Duration can be used to describe a quantity of time in seconds and nonoseconds
        durationStuff();

        // compatibility between old and new API was added
        compatibilityStuff();

        // formatting is pretty easy on new API
        formattingStuff();

        System.out.println();
    }

    private void localDateStuff() {
        // get current date
        LocalDate now = LocalDate.now();
        System.out.println("LocalDate now: " + now);
        // get specific date from parts
        LocalDate fromParts = LocalDate.of(2020, 7, 1);
        System.out.println("LocalDate of: " + fromParts);
        // parse date form ISO format
        System.out.println("LocalDate parsed: " + LocalDate.parse("2020-07-01"));
        // its easy to add days/months/years
        System.out.println("LocalDate now + 1 day: " + now.plusDays(1));
        // its easy to substract days/months/years
        System.out.println("LocalDate now - 1 day: " + now.minusDays(1));
        // more generic way is to use ChronoUnits
        System.out.println("LocalDate now - 2 decades: " + now.minus(2, ChronoUnit.DECADES));
        // date parts can be easily extracted
        System.out.println("LocalDate now year: " + now.getYear());
        // likewise more indirect values can be extracted
        System.out.println("LocalDate now day of week: " + now.getDayOfWeek());
        System.out.println("LocalDate now leapyear? " + now.isLeapYear());
        // comparison is fairly easy
        System.out.println(now + " before " + fromParts + " ? " + now.isBefore(fromParts));
        System.out.println(now + " after " + fromParts + " ? " + now.isAfter(fromParts));
        // transformations are also easy to use
        System.out.println("now to date with time at start: " + now.atStartOfDay());
        System.out.println("last day of current month: " + now.with(TemporalAdjusters.lastDayOfMonth()));

        System.out.println();
    }

    private void localTimeStuff() {
        // get current time
        LocalTime now = LocalTime.now();
        System.out.println("LocalTime now: " + now);
        // get specific time from parts
        LocalTime fromParts = LocalTime.of(13, 55,11,12);
        System.out.println("LocalTime of: " + fromParts);
        // parse time form string
        System.out.println("LocalTime parsed: " + LocalTime.parse("08:40"));
        // addition and subtraction is easy
        System.out.println("LocalTime now + 15 minutes: " + now.plusMinutes(15));
        System.out.println("LocalTime now + 4 hours: " + now.plus(4, ChronoUnit.HOURS));
        System.out.println("LocalTime now - 2 hours: " + now.minusHours(2));
        System.out.println("LocalTime now - 33 minutes: " + now.minus(33, ChronoUnit.MINUTES));
        // date parts can be easily extracted
        System.out.println("LocalTime now seconds: " + now.getSecond());
        // comparison is fairly easy
        System.out.println(now + " before " + fromParts + " ? " + now.isBefore(fromParts));
        System.out.println(now + " after " + fromParts + " ? " + now.isAfter(fromParts));

        System.out.println();
    }

    private void localDateTimeStuff() {
        // get current date
        LocalDateTime now = LocalDateTime.now();
        System.out.println("LocalDateTime now: " + now);
        // get specific date from parts
        LocalDateTime fromParts = LocalDateTime.of(2020, 7, 1, 7, 44);
        System.out.println("LocalDateTime of: " + fromParts);
        // parse date form ISO format
        System.out.println("LocalDateTime parsed: " + LocalDateTime.parse("2020-07-01T07:44:00"));
        // addition and subtraction is easy
        System.out.println("LocalDateTime now + 15 minutes: " + now.plusMinutes(15));
        System.out.println("LocalDateTime now + 4 days: " + now.plus(4, ChronoUnit.DAYS));
        System.out.println("LocalDateTime now - 2 hours: " + now.minusHours(2));
        System.out.println("LocalDateTime now - 33 years: " + now.minus(33, ChronoUnit.YEARS));
        // date parts can be easily extracted
        System.out.println("LocalDateTime now year: " + now.getYear());
        // comparison is fairly easy
        System.out.println(now + " before " + fromParts + " ? " + now.isBefore(fromParts));
        System.out.println(now + " after " + fromParts + " ? " + now.isAfter(fromParts));

        System.out.println();
    }

    private void ZonedDateTimeStuff() {
        // zones are identified by a ZoneId
        // get a list of all zones
        System.out.println("available ZoneIds: " + ZoneId.getAvailableZoneIds());
        // get a specific ZoneId
        ZoneId zoneId = ZoneId.of("America/Belize");
        System.out.println("ZoneId of: " + zoneId);
        // convert LocalDateTime to specific zone
        System.out.println("ZonedDateTime of: " + ZonedDateTime.of(LocalDateTime.now(), zoneId));
        // parse ZonedDateTime from String
        System.out.println("ZonedDateTime parse: " + ZonedDateTime.parse("2004-12-24T15:12:30+01:00[Europe/Paris]"));
        // convert LocalDateTime with offset
        System.out.println("OffsetDateTime of: " + OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.of("+06:00")));
        // convert to LocalDateTime in other zone
        System.out.println("LocalDateTime now in Belize: " + ZonedDateTime.now(zoneId).toLocalDateTime());
        // convert to ZonedDateTime in belize
        System.out.println("ZonedDateTime now in Belize: " + ZonedDateTime.now().withZoneSameInstant(zoneId));

        // rest of API is pretty similar to other classes
        System.out.println();
    }

    private void periodStuff() {
        LocalDate now = LocalDate.now();
        // a Period can be created with of
        Period period = Period.of(1, 3, 22);
        // Period can be used to manipulate a LocalDate
        System.out.println("LocalDate now + 1 y 3 m 22 d: " + now.plus(period));
        // Period can be used to get the difference between LocalDate
        System.out.println("Between: " + Period.between(now, now.plus(period)));
        // a specific field!!! can be extracted from the difference
        System.out.println("Between days: " + Period.between(now, now.plus(period)).getDays());
        // to extract the difference in days (not the field days!) ChronoUnit must be used
        System.out.println("Between days: " + ChronoUnit.DAYS.between(now, now.plus(period)));

        System.out.println();
    }

    private void durationStuff() {
        LocalTime now = LocalTime.now();
        // a Duration can be created with of
        Duration duration = Duration.ofSeconds(15604);
        // Duration can be used to manipulate a LocalTime
        System.out.println("LocalDate now + 4 h 20 m 4 s: " + now.plus(duration));
        // Period can be used to get the difference between LocalDate
        System.out.println("Between: " + Duration.between(now, now.plus(duration)));
        // nonos or seconds can be extracted from the difference
        System.out.println("Between seconds: " + Duration.between(now, now.plus(duration)).get(ChronoUnit.SECONDS));
        // to extract the difference in minutes ChronoUnit must be used
        System.out.println("Between minutes: " + ChronoUnit.MINUTES.between(now, now.plus(duration)));

        System.out.println();
    }

    private void compatibilityStuff() {
        // ofInstant and toInstant can be used to convert a Date or Calendar object to new LocalDateTime
        // as the old objects always hold utc time u have to say for which zone you want the local date/time
        ZoneId utc = ZoneId.of("UTC");
        System.out.println("Date now with system default: " + LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
        System.out.println("Calendar now with system default: " + LocalDateTime.ofInstant(new GregorianCalendar().toInstant(), ZoneId.systemDefault()));
        System.out.println("Date now with utc: " + LocalDateTime.ofInstant(new Date().toInstant(), utc));
        System.out.println("Calendar now with utc: " + LocalDateTime.ofInstant(new GregorianCalendar().toInstant(), utc));

        // conversion to old objects is also possible with instant
        // on that way it is also necessary to tell which timezone is represented form LocalDateTime
        System.out.println("LocalDateTime now with utc: " + Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)));
        ZoneOffset offset = ZoneId.systemDefault().getRules().getOffset(LocalDateTime.now());
        System.out.println("LocalDateTime now with utc: " + Date.from(LocalDateTime.now().toInstant(offset)));

        System.out.println();
    }

    private void formattingStuff() {
        LocalDateTime now = LocalDateTime.now();
        // DateTimeFormatter can be used to covert LocalDateTime to String
        System.out.println("DateTimeFormatter ISO_DATE: " +  now.format(DateTimeFormatter.ISO_DATE));
        // LocalDateTime toString ist full ISO format
        System.out.println("LocalDateTime toString: " +  now.toString());
        // LocalDateTime toString ist full ISO format
        System.out.println("DateTimeFormatter ISO_DATE_TIME: " +  now.format(DateTimeFormatter.ISO_DATE_TIME));
        // ofcourse you can use patterns
        System.out.println("DateTimeFormatter patter: " +  now.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
        // more generic is the use of localization
        System.out.println("DateTimeFormatter localization: " +  now.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(Locale.GERMAN)));
    }

    private void parallelArraySorting() {
        System.out.println("### parallel array sorting ###");

        // it is now possible to sort an array parallel

        int[] arraySizes = { 10000, 100000, 1000000, 10000000 };

        for (int arraySize : arraySizes ) {

            int[] intArray = new int[arraySize];
            Random random = new Random();

            System.out.println("array size: " + arraySize);

            for (int i = 0; i < arraySize; i++) {
                intArray[i] = random.nextInt(arraySize) + random.nextInt(arraySize);
            }

            int[] forSequential = Arrays.copyOf(intArray, intArray.length);
            int[] forParallel = Arrays.copyOf(intArray, intArray.length);

            long startTime = System.currentTimeMillis();
            Arrays.sort(forSequential);
            long endTime = System.currentTimeMillis();

            System.out.println("sequential sort milliseconds: " + (endTime - startTime));

            startTime = System.currentTimeMillis();
            Arrays.parallelSort(forParallel);
            endTime = System.currentTimeMillis();

            System.out.println("parallel Sort milliseconds: " + (endTime - startTime));

        }

        // can save time on very big arrays

        System.out.println();
    }

    private void annotations() {
        System.out.println("### typed and repeatable annotations ###");
        // annotation can now be applied to any type use, to add logical information for example
        @NonNull String test = "";
        List<@NonNull String> list;

        // furthermore repeatable annotations were introduced
        NameGenerator.listAll();
    }

    private void functionalInterfaces() {
        System.out.println("### functional interfaces ###");

        // with functional interfaces java has the capability of storing and passing methods in variables
        // there are several classes under java.util.function for different requirements of the method to hold
        // eq how many params does it need and does it return anything
        // functional interfaces can be filled with anonymous classes, method references or lambdas
        // the functional interfaces can be categorized
        // - function takes arguments and returns a result
        // - supplier doesn't take arguments but returns a result
        // - consumer takes arguments but doesn't return a result
        // - runnable as already know takes no arguments and returns no result
        // if a functional interfaces needs 2 params its name contains Bi or Binary
        // all these functional interfaces work with generics, to work with primitive types explicit classes were addes like IntConsumer, IntFunction
        // there are some special types like predicates who receive a value and return a boolean value
        // and operators where param type and result type are identical
        FunctionalInterfaceExample fie = new FunctionalInterfaceExample();
        LongBinaryOperator addFunction = fie.run();

        System.out.println("add function as functional interface in Starter: "  + addFunction.applyAsLong(8, 6));

        System.out.println();
    }

    private void lambdas() {
        System.out.println("### lambdas ###");

        // lambdas are used to declare implementations of functional interfaces
        // basically they are much better to read and understand than anonymous classes
        // there is a major difference between lambdas an anonymous classes
        // while anonymous classes declare a new scope, lambdas do not
        long[] count = {0};
        Runnable counter = new Runnable() {
            @Override
            public void run() {
                long count = 0;
                count++;
            }
        };
        counter.run();
        System.out.println("anonymous class -> count should be 0: " + count[0]);

        // compile error count already defined
//        counter = () -> {
//            long count = 0;
//        };
        counter = () -> count[0]++;
        counter.run();
        System.out.println("lambda -> count should be 1: " + count[0]);

        System.out.println();
    }

    private void streams() {
        System.out.println("### streams ###");

        String[] arr = { "eins", "zwei", "drei", "vier" };
        List<String> stringList = List.of("eins", "zwei", "drei", "vier");

        // to create a stream from an array use Arrays.stream
        Stream<String> stream = Arrays.stream(arr);
        // to create a stream from literals use Stream.of
        stream = Stream.of("eins", "zwei", "drei", "vier");
        // for collections a stream() method was introduced
        stream = stringList.stream();
        // to perform multithreaded operations on a collection use parallelStream
        stream = stringList.parallelStream();

        // stream api provides a variety of intermediate and terminal operations
        // intermediate operations (e.g. distinct) perform something and return a new stream -> chaining is possible
        // terminal operations (e.g. count) return a result
        stringList.stream().distinct().count();

        // loops can be replaced
        for (String item : stringList) {
            System.out.println("foreach loop: " + item);
        }
        // one-line in streams
        stringList.stream().forEach(item -> System.out.println("foreach stream: " + item));
        // also there is an even shorter version for iterables
        stringList.forEach(item -> System.out.println("foreach stream: " + item));

        // stream api allows easy filtering
        Stream<String> filteredStream = stringList.stream().filter(item -> item.contains("ei"));
        System.out.print("filtered stream (ei): ");
        filteredStream.forEach(item -> System.out.print(item + ","));
        System.out.println();

        // with stream map, a new stream with different type can be created
        // a logic to convert from one type to the other is needed
        Stream<Integer> mappedStream = stringList.stream().map(this::stringNumberToInt);
        System.out.print("mapped stream (numbers: ");
        mappedStream.forEach(item -> System.out.print(item + ","));
        System.out.println();
        // if the elements of your stream contain an own sequence of elements and you want to get the list of all this subelements you can use flatMap
        List<Recipe> recipes = Recipe.generateRecipes();
        Stream<Ingredient> ingredients = recipes.stream().flatMap(element -> element.getIngredients().stream());
        System.out.print("ingredients: ");
        ingredients.forEach(item -> System.out.print(item+ ","));
        System.out.println();

        // for matching there are three self-explanatory methods
        boolean containsAnyDrei = stringList.stream().anyMatch(element -> element.contains("drei")); // true
        System.out.println("any drei? " + containsAnyDrei);
        boolean allElementsAreDrei = stringList.stream().allMatch(element -> element.contains("drei")); // false
        System.out.println("all drei? " + allElementsAreDrei);
        boolean noElementIsDrei = stringList.stream().noneMatch(element -> element.contains("drei")); // false
        System.out.println("none drei? " + noElementIsDrei);

        // a stream can be reduced to a simple type, example concatenation of strings
        String reduced = stringList.stream().reduce("null", (a, b) -> String.join(",", a, b));
        System.out.println("reduce: " + reduced);

        // to convert the stream back to a collection the collect method can be used
        // the new collectors provides a collector for most usecases
        System.out.println("collect list: " + stringList.stream().filter(item -> item.contains("ei")).collect(Collectors.toList()));
        System.out.println("collect set: " + stringList.stream().filter(item -> item.contains("ei")).collect(Collectors.toSet()));
        System.out.println("collect map: " + stringList.stream().filter(item -> item.contains("ei")).collect(Collectors.toMap(this::stringNumberToInt, item -> item)));
        System.out.println("collect join: " + stringList.stream().filter(item -> item.contains("ei")).collect(Collectors.joining(",")));

        System.out.println();
    }

    private int stringNumberToInt(String number) {
        switch (number) {
            case "eins":
                return 1;
            case "zwei":
                return 2;
            case "drei":
                return 3;
            case "vier":
                return 4;
            default:
                return -1;
        }
    }

}
