/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.resource;

import com.smartcampus.model.Sensor;
import com.smartcampus.store.DataStore;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

     
    @GET
    public Collection<Sensor> getSensors(@QueryParam("type") String type) {

        if (type == null) {
            return DataStore.sensors.values();
        }

        List<Sensor> filtered = new ArrayList<>();

        for (Sensor s : DataStore.sensors.values()) {
            if (s.getType().equalsIgnoreCase(type)) {
                filtered.add(s);
            }
        }

        return filtered;
    }

    
    @POST
    public Response addSensor(Sensor sensor) {

         
        if (!DataStore.rooms.containsKey(sensor.getRoomId())) {
            return Response.status(422)
                    .entity("{\"error\":\"Room does not exist\"}")
                    .build();
        }

        DataStore.sensors.put(sensor.getId(), sensor);

         
        DataStore.rooms.get(sensor.getRoomId())
                .getSensorIds()
                .add(sensor.getId());

        return Response.status(201).entity(sensor).build();
    }
    
    @Path("/{id}/readings")
    public SensorReadingResource getReadingResource() {
    return new SensorReadingResource();
    }
}
    
