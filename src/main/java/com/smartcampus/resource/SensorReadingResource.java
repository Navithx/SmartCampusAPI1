/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.resource;

import com.smartcampus.model.*;
import com.smartcampus.store.DataStore;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    @GET
    public List<SensorReading> getReadings(@PathParam("id") String sensorId) {
        return DataStore.readings.getOrDefault(sensorId, new ArrayList<>());
    }

    @POST
    public SensorReading addReading(@PathParam("id") String sensorId, SensorReading reading) {

        
        DataStore.readings.putIfAbsent(sensorId, new ArrayList<>());
        DataStore.readings.get(sensorId).add(reading);

         
        Sensor sensor = DataStore.sensors.get(sensorId);
        sensor.setCurrentValue(reading.getValue());

        return reading;
    }
}
