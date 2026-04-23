package com.smartcampus.store;

import com.smartcampus.model.Room;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
 * Central in-memory storage.
 * ConcurrentHashMap is used to reduce race-condition risks when handling requests.
 */
public class InMemoryStore {

    private static final Map<String, Room> rooms = new ConcurrentHashMap<>();
    private static final Map<String, Sensor> sensors = new ConcurrentHashMap<>();
    private static final Map<String, List<SensorReading>> readings = new ConcurrentHashMap<>();

    static {
        Room r1 = new Room("LIB-301", "Library Quiet Study", 50);
        Room r2 = new Room("ENG-101", "Engineering Lab", 35);

        rooms.put(r1.getId(), r1);
        rooms.put(r2.getId(), r2);

        Sensor s1 = new Sensor("TEMP-001", "Temperature", "ACTIVE", 24.5, "LIB-301");
        Sensor s2 = new Sensor("CO2-001", "CO2", "MAINTENANCE", 800.0, "ENG-101");

        sensors.put(s1.getId(), s1);
        sensors.put(s2.getId(), s2);

        rooms.get("LIB-301").getSensorIds().add(s1.getId());
        rooms.get("ENG-101").getSensorIds().add(s2.getId());

        readings.put(s1.getId(), new ArrayList<>());
        readings.put(s2.getId(), new ArrayList<>());
    }

    private InMemoryStore() {
    }

    public static Map<String, Room> getRooms() {
        return rooms;
    }

    public static Map<String, Sensor> getSensors() {
        return sensors;
    }

    public static Map<String, List<SensorReading>> getReadings() {
        return readings;
    }
}