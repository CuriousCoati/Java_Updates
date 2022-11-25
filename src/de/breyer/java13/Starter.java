package de.breyer.java13;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class Starter {

    public static void main(String[] args) {
        Starter starter = new Starter();

        starter.fileSystem();
        starter.byteBuffer();

        // CDS was further enhanced by dynamic archiving

        // Z GC was enhanced to return unused heap memory to the os
        // additionally ZGC supports up to 16TB heap size (4TB until now)

        // sockets were modernized and uses the nio principle
        // that concerns only the underlying impl, api was not changed
    }

    private void byteBuffer() {
        System.out.println("### file system ###");

        // java 13 added features to byteBuffer to work independent of buffer position
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(100);

        // write to a specific location without moving the buffer
        byteBuffer.put(10, "Hello".getBytes(StandardCharsets.UTF_8));
        System.out.println("buffer position: " + byteBuffer.position());

        // read specific location without moving the buffer
        byte[] read = new byte[5];
        byteBuffer.get(10, read);
        System.out.println("read from buffer: " + new String(read));
        System.out.println("buffer position: " + byteBuffer.position());

        // likewise slice() gives a view of the specified buffer part
        ByteBuffer sliced = byteBuffer.slice(10, 5);
        System.out.println("sliced buffer: " + sliced);
        System.out.println("orig buffer: " + byteBuffer);


        System.out.println();
    }

    private void fileSystem() {
        System.out.println("### file system ###");

        try {
            // with newFileSystem a pseudo filesystem can be created
            // e.g. to open a zip and use it as a filesystem
            FileSystem zipFs = FileSystems.newFileSystem(Paths.get("", "src/de/breyer/java13/demo.zip"), new HashMap<>());

            System.out.println("content of zip:");
            Files.walk(zipFs.getPath("")).forEach(path -> System.out.println(path));

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println();
    }

}
