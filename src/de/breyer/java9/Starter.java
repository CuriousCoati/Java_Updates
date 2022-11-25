package de.breyer.java9;

import java.awt.Image;
import java.awt.image.BaseMultiResolutionImage;
import java.awt.image.MultiResolutionImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.SubmissionPublisher;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;

public class Starter {

    public static void main(String[] args) {
        Starter starter = new Starter();

        // biggest change are java modules, see moduleOne and moduleTwo for brief intro

        starter.httpClient();
        starter.processApi();
        starter.tryWithResources();
        starter.diamondOperator();
        starter.privateMethodsOnInterfaces();
        starter.multiResolutionImage();
        starter.variableHandles();
        starter.reactiveStreams();
        starter.immutableSet();
        starter.optionalsInStreams();

        // there is a new external tool JShell for REPL (read-eval-print loop)
        // it can be used to test code snippets and is located under jdk9/bin/jshell

        // moreover the jcmd command tool has a few new subcommands

        // jvm can be started with a -xlog flag to control jvm interal logging
    }

    private void httpClient() {
        System.out.println("### http client ###");

        // java 9 introduced a new http client

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://worldtimeapi.org/api/timezone/Europe/Berlin"))
                    .GET()
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, BodyHandlers.ofString());

            System.out.println("response: " + response);
            System.out.println("body: " + response.body());

        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println();
    }

    private void processApi() {
        System.out.println("### process api ###");

        // process api can be used to get info about the os process

        // allProcesses retrieves a list of pid visible to the current process
        System.out.println("visible processes: " + ProcessHandle.allProcesses().count());

        // current gives the current process
        ProcessHandle self = ProcessHandle.current();
        System.out.println("current pid: " + self.pid());
        System.out.println("current args: " + self.info().arguments());
        System.out.println("current cmd: " + self.info().commandLine());
        System.out.println("current start instant: " + self.info().startInstant());
        System.out.println("current cpu duration: " + self.info().totalCpuDuration());

        // with destroy process could be terminated
        self.children().forEach(ProcessHandle::destroy);

        System.out.println();
    }

    private void tryWithResources() {
        System.out.println("### try with resources ###");

        // with java 9 (auto)closables can be declared before the try
        ExampleAutoCloseable eac = new ExampleAutoCloseable();
        try (eac) {
            eac.doStuff();
        } catch (Exception e) {
            System.out.println(e.getClass().getSimpleName() + " caught " + e.getMessage());
        }

        System.out.println();
    }

    private void diamondOperator() {
        System.out.println("### diamond operator ###");

        // with java 9 diamond operator can be used with anonymous classes
        ExampleGeneric<String> eg = new ExampleGeneric<>() { // until 9 compile would complain here
            @Override
            void print(String printable) {
                System.out.println(printable);
            }
        };
        eg.print("diamond operator with anonymous class");

        System.out.println();
    }

    private void privateMethodsOnInterfaces() {
        System.out.println("### diamond operator ###");

        // with java 9 interfaces can have private methods
        ExampleInterface interfaceImpl = () -> {
            // as the methods are private, they are not accessible on implementations
            System.out.println("doStuff -> can't access private methods");
        };
        interfaceImpl.doStuff();
        interfaceImpl.defaultMethod();

        System.out.println();
    }

    private void multiResolutionImage() {
        System.out.println("### multi resolution image ###");

        // with MultiResolutionImage different resolution of the same image can be treated as one object

        try {

            List<String> imgUrls = List.of("img_large.png", "img_medium.png", "img_small.png");

            List<Image> images = new ArrayList<>();

            for (String url : imgUrls) {
                images.add(ImageIO.read(Objects.requireNonNull(getClass().getResource(url))));
            }

            // create MultiResolutionImage from images
            MultiResolutionImage multiResolutionImage = new BaseMultiResolutionImage(images.toArray(new Image[0]));

            // all variant can be read with getResolutionVariants()
            List<Image> variants = multiResolutionImage.getResolutionVariants();

            System.out.println("resolutions: " + variants.size());
            variants.forEach(System.out::println);

            // getResolutionVariant can be used to get best fitting image for wanted resolution
            Image variant1 = multiResolutionImage.getResolutionVariant(1800, 1400);
            System.out.println("wanted resolution 1800 x 1400 -> " + variant1.getWidth(null) + " x " + variant1.getHeight(null));

            Image variant2 = multiResolutionImage.getResolutionVariant(800, 600);
            System.out.println("wanted resolution 800 x 600 -> " + variant2.getWidth(null) + " x " + variant2.getHeight(null));
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println();
    }

    private void variableHandles() {
        System.out.println("### variable handles ###");

        // with java 9 var handles were introduces
        // simply said these are typed references to a variable
        // goal is to define a standard for invoking java.util.concurrent.atomic and sun.misc.Unsafe
        VarHandleExample example = new VarHandleExample();
        example.doVarHandleStuff();

        System.out.println();
    }

    private void reactiveStreams() {
        System.out.println("### reactive streams ###");

        try {
            // java 9 added reactive streams which are defined by the reactive manifesto
            // it is a standard for asynchronous processing without blocking
            SubmissionPublisher<ExampleEvent> publisher = new SubmissionPublisher<>();
            ExampleSubscriber subscriber = new ExampleSubscriber();
            ExampleProcessor processor = new ExampleProcessor();
            publisher.subscribe(subscriber);
            publisher.subscribe(processor);
            processor.subscribe(subscriber);

            List<ExampleEvent> events = List.of(new ExampleEvent("INIT"), new ExampleEvent("ADD"), new ExampleEvent("SUB"), new ExampleEvent("ERROR"),
                    new ExampleEvent("MUL"), new ExampleEvent("END"));

            System.out.println("subscribers [PUB]: " + publisher.getNumberOfSubscribers());
            System.out.println("subscribers [PRO]: " + processor.getNumberOfSubscribers());
            events.forEach(publisher::submit);
            publisher.close();

            System.out.println("processed messages [SUB]: " + subscriber.getProcessedMessages());
            System.out.println("processed messages [PRO]: " + processor.getProcessedMessages());
            Thread.sleep(1000);
            System.out.println("processed messages [SUB]: " + subscriber.getProcessedMessages());
            System.out.println("processed messages [PRO]: " + processor.getProcessedMessages());



        } catch(InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println();
    }

    private void immutableSet() {
        System.out.println("### immutable set ###");

        // with java 9 immutable sets can be easily created with Set.of
        System.out.println("Set.of(): " + Set.of(1,2,3,4,5));
        // array can be converted to set
        String[] array = { "one", "two", "three", "four" };
        System.out.println("Set.of(): " + Set.of(array));


        System.out.println();
    }

    private void optionalsInStreams() {
        System.out.println("### optionals in streams ###");

        // with java 9 optionals can be easily used in streams
        List<Optional<String>> optionals = new ArrayList<>();
        optionals.add(Optional.empty());
        optionals.add(Optional.of("one"));
        optionals.add(Optional.empty());
        optionals.add(Optional.of("two"));
        optionals.add(Optional.of("three"));
        optionals.add(Optional.empty());
        optionals.add(Optional.empty());

        // e.g. remove empty optionals
        List<String> filteredList = optionals.stream()
                .flatMap(Optional::stream)
                .collect(Collectors.toList());

        System.out.println("size filteredList: " + filteredList.size());
        System.out.println("filteredlist: " + filteredList);

        System.out.println();
    }
}

