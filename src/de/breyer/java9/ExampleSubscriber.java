package de.breyer.java9;

import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

public class ExampleSubscriber implements Subscriber<ExampleEvent> {

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
        System.out.println("ExampleSubscriber received event: " + event);
        processedMessages++;
        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {
        System.out.println("ExampleSubscriber completed");
    }

}
