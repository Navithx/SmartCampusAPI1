/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.exception;

import javax.ws.rs.core.*;
import javax.ws.rs.ext.*;
import javax.ws.rs.Produces;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable ex) {

        return Response.status(500)
                .entity("{\"error\":\"Internal Server Error\"}")
                .build();
    }
}