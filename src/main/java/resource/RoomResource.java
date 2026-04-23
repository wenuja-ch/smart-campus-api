package com.smartcampus.resource;

import com.smartcampus.exception.ResourceNotFoundException;
import com.smartcampus.exception.RoomNotEmptyException;
import com.smartcampus.model.Room;
import com.smartcampus.store.InMemoryStore;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    private final Map<String, Room> rooms = InMemoryStore.getRooms();

    @GET
    public List<Room> getAllRooms() {
        return new ArrayList<>(rooms.values());
    }

    @POST
    public Response createRoom(Room room, @Context UriInfo uriInfo) {
        if (room == null || room.getId() == null || room.getId().isBlank()) {
            throw new BadRequestException("Room id is required.");
        }
        if (room.getName() == null || room.getName().isBlank()) {
            throw new BadRequestException("Room name is required.");
        }
        if (rooms.containsKey(room.getId())) {
            throw new ClientErrorException("Room with this id already exists.", Response.Status.CONFLICT);
        }
        if (room.getSensorIds() == null) {
            room.setSensorIds(new ArrayList<>());
        }

        rooms.put(room.getId(), room);

        URI location = uriInfo.getAbsolutePathBuilder().path(room.getId()).build();
        return Response.created(location).entity(room).build();
    }

    @GET
    @Path("/{roomId}")
    public Room getRoomById(@PathParam("roomId") String roomId) {
        Room room = rooms.get(roomId);
        if (room == null) {
            throw new ResourceNotFoundException("Room not found: " + roomId);
        }
        return room;
    }

    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = rooms.get(roomId);
        if (room == null) {
            throw new ResourceNotFoundException("Room not found: " + roomId);
        }

        if (room.getSensorIds() != null && !room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException(
                    "Room " + roomId + " cannot be deleted because it still contains assigned sensors."
            );
        }

        rooms.remove(roomId);
        return Response.ok()
                .entity(Map.of(
                        "message", "Room deleted successfully",
                        "roomId", roomId
                ))
                .build();
    }
}