package de.breyer.java9;

import java.util.concurrent.Flow.Processor;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.SubmissionPublisher;

public class ExampleProcessor extends SubmissionPublisher<ExampleEvent> implements Processor<ExampleEvent, ExampleEvent> {

    private Subscription subscription;
    private int processedMessages = 0;

    public int getProcessedMessages() {
        return processedMessages;
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
    }

    @Override
    public void onNext(ExampleEvent event) {
        System.out.println("ExampleProcessor received event: " + event);
        processedMessages++;
        submit(event);
        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {
        System.out.println("ExampleProcessor completed");
        close();
    }
}
