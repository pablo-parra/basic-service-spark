package org.example;

import com.google.gson.GsonBuilder;
import org.example.client.ExternalService;
import org.example.rest.Controller;

public class Application {
    public static void main(String[] args) {

        final var gson = new GsonBuilder().create();
        final var employeeService = new ExternalService(gson);

        new Controller(gson, employeeService);
    }
}