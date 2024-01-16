package org.example.functioninterface.sample;

import java.util.function.Supplier;

public class SupplierSample {
    public static void main(String[] args) {
        Supplier<String> getMessage = () -> "Hello! World";
        System.out.println(getMessage.get());
    }
}
