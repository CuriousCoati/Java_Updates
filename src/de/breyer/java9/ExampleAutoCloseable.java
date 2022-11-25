package de.breyer.java9;

import java.io.IOException;

public class ExampleAutoCloseable implements AutoCloseable{

    @Override
    public void close() throws Exception {
        System.out.println("ExampleAutoCloseable -> closed");
    }

    public void doStuff() throws IOException {
        throw new IOException("Test");
    }
}
