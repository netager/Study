package org.example.functioninterface.sample;

import org.example.functioninterface.CustomFunctionalInterface;

public class SampleCalled {

    public static void main(String[] args) {
        CustomFunctionalInterface customInterface = (message) -> System.out.println(message);
//        CustomFunctionalInterface customInterface = System.out::println;

        String message = "Hello! Around Hub Studio";
        customInterface.called(message);

        customInterface.printHello();
    }
}
