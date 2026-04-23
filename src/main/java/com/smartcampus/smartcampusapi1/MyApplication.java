/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.smartcampusapi1;

import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/api/v1")
public class MyApplication extends ResourceConfig {

    public MyApplication() {
        packages(
            "com.smartcampus.resource",
            "com.smartcampus.exception",
            "com.smartcampus.filter"
        );
    }
}