/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.exception;

import javax.ws.rs.core.*;
import javax.ws.rs.ext.*;
import javax.ws.rs.NotFoundException;

@Provider
public class NotFoundMapper implements ExceptionMapper<NotFoundException> {

    @Override
    public Response toResponse(NotFoundException ex) {
        return Response.status(404)
                .entity("{\"error\":\"Resource not found\"}")
                .type("application/json")
                .build();
    }
}
