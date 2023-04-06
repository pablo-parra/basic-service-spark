package org.example;

import com.google.gson.GsonBuilder;
import org.example.client.ExternalService;
import org.example.rest.Controller;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        final var gson = new GsonBuilder().create();
        final var employeeService = new ExternalService(gson);

        new Controller(gson, employeeService);
    }
}