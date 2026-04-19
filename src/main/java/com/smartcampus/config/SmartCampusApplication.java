package com.smartcampus.config;

import com.smartcampus.filter.LoggingFilter;
import com.smartcampus.mapper.GenericExceptionMapper;
import com.smartcampus.mapper.LinkedResourceNotFoundExceptionMapper;
import com.smartcampus.mapper.ResourceNotFoundExceptionMapper;
import com.smartcampus.mapper.RoomNotEmptyExceptionMapper;
import com.smartcampus.mapper.SensorUnavailableExceptionMapper;
import com.smartcampus.resource.DiscoveryResource;
import com.smartcampus.resource.RoomResource;
import com.smartcampus.resource.SensorResource;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("api/v1")
public class SmartCampusApplication extends ResourceConfig {

    public SmartCampusApplication() {
        register(JacksonFeature.class);

        register(DiscoveryResource.class);
        register(RoomResource.class);
        register(SensorResource.class);

        register(RoomNotEmptyExceptionMapper.class);
        register(LinkedResourceNotFoundExceptionMapper.class);
        register(SensorUnavailableExceptionMapper.class);
        register(ResourceNotFoundExceptionMapper.class);
        register(GenericExceptionMapper.class);

        register(LoggingFilter.class);
    }
}