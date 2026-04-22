/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.smartcampusapi1;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import java.net.URI;

public class Main {

    public static final String BASE_URI = "http://localhost:8080/";

    public static void main(String[] args) {

        // This scans your resource package automatically
        ResourceConfig config = new ResourceConfig()
                .packages("com.smartcampus.resource","com.smartcampus.exception"," com.smartcampus.filter");

        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(
                URI.create(BASE_URI),
                config
        );

        System.out.println("Server running at: " + BASE_URI);

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}