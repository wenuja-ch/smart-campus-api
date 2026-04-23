package com.smartcampus.resource;

import com.smartcampus.exception.ResourceNotFoundException;
import com.smartcampus.exception.SensorUnavailableException;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;
import com.smartcampus.store.InMemoryStore;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    private final String sensorId;

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    @GET
    public List<SensorReading> getAllReadings() {
        ensureSensorExists();
        return InMemoryStore.getReadings().get(sensorId);
    }

    @POST
    public Response addReading(SensorReading reading) {
        Sensor sensor = ensureSensorExists();

        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException(
                    "Sensor " + sensorId + " is in MAINTENANCE mode and cannot accept new readings."
            );
        }

        if ("OFFLINE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException(
                    "Sensor " + sensorId + " is OFFLINE and cannot accept new readings."
            );
        }

        if (reading == null) {
            throw new BadRequestException("Reading body is required.");
        }

        if (reading.getId() == null || reading.getId().isBlank()) {
            reading.setId(UUID.randomUUID().toString());
        }

        if (reading.getTimestamp() <= 0) {
            reading.setTimestamp(System.currentTimeMillis());
        }

        InMemoryStore.getReadings().get(sensorId).add(reading);

        // Side effect required by the coursework:
        sensor.setCurrentValue(reading.getValue());

        return Response.status(Response.Status.CREATED)
                .entity(Map.of(
                        "message", "Reading added successfully",
                        "sensorId", sensorId,
                        "reading", reading
                ))
                .build();
    }

    private Sensor ensureSensorExists() {
        Sensor sensor = InMemoryStore.getSensors().get(sensorId);
        if (sensor == null) {
            throw new ResourceNotFoundException("Sensor not found: " + sensorId);
        }
        if (!InMemoryStore.getReadings().containsKey(sensorId)) {
            throw new ResourceNotFoundException("Reading collection not found for sensor: " + sensorId);
        }
        return sensor;
    }
}