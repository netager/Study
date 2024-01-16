package org.example.functioninterface.sample;

import java.util.function.Consumer;

public class ConsumerSample {

    public static void main(String[] args) {

        Consumer<String> print = (message) -> System.out.println(("Message : " + message));
        print.accept("Hello! World");
    }
}
