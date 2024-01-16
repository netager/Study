package org.example.functioninterface;

@FunctionalInterface
public interface CustomFunctionalInterface {
    void called(String message);

    default void printHello() { System.out.println("Hello! World");}
}
