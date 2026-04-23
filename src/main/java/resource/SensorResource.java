package com.smartcampus.resource;

import com.smartcampus.exception.LinkedResourceNotFoundException;
import com.smartcampus.exception.ResourceNotFoundException;
import com.smartcampus.model.Room;
import com.smartcampus.model.Sensor;
import com.smartcampus.store.InMemoryStore;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    private final Map<String, Sensor> sensors = InMemoryStore.getSensors();
    private final Map<String, Room> rooms = InMemoryStore.getRooms();

    @GET
    public List<Sensor> getSensors(@QueryParam("type") String type) {
        List<Sensor> all = new ArrayList<>(sensors.values());

        if (type == null || type.isBlank()) {
            return all;
        }

        return all.stream()
                .filter(sensor -> sensor.getType() != null && sensor.getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{sensorId}")
    public Sensor getSensorById(@PathParam("sensorId") String sensorId) {
        Sensor sensor = sensors.get(sensorId);
        if (sensor == null) {
            throw new ResourceNotFoundException("Sensor not found: " + sensorId);
        }
        return sensor;
    }

    @POST
    public Response createSensor(Sensor sensor, @Context UriInfo uriInfo) {
        if (sensor == null || sensor.getId() == null || sensor.getId().isBlank()) {
            throw new BadRequestException("Sensor id is required.");
        }
        if (sensor.getType() == null || sensor.getType().isBlank()) {
            throw new BadRequestException("Sensor type is required.");
        }
        if (sensor.getRoomId() == null || sensor.getRoomId().isBlank()) {
            throw new BadRequestException("roomId is required.");
        }
        if (sensor.getStatus() == null || sensor.getStatus().isBlank()) {
            sensor.setStatus("ACTIVE");
        }
        if (sensors.containsKey(sensor.getId())) {
            throw new ClientErrorException("Sensor with this id already exists.", Response.Status.CONFLICT);
        }

        Room room = rooms.get(sensor.getRoomId());
        if (room == null) {
            throw new LinkedResourceNotFoundException(
                    "Cannot create sensor because roomId '" + sensor.getRoomId() + "' does not exist."
            );
        }

        sensors.put(sensor.getId(), sensor);
        room.getSensorIds().add(sensor.getId());
        InMemoryStore.getReadings().put(sensor.getId(), new ArrayList<>());

        URI location = uriInfo.getAbsolutePathBuilder().path(sensor.getId()).build();
        return Response.created(location).entity(sensor).build();
    }

    @Path("/{sensorId}/readings")
    public SensorReadingResource getSensorReadingResource(@PathParam("sensorId") String sensorId) {
        if (!sensors.containsKey(sensorId)) {
            throw new ResourceNotFoundException("Sensor not found: " + sensorId);
        }
        return new SensorReadingResource(sensorId);
    }
}