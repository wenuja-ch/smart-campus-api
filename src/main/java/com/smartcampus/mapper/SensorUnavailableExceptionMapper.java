package com.smartcampus.mapper;

import com.smartcampus.exception.SensorUnavailableException;
import com.smartcampus.model.ApiError;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class SensorUnavailableExceptionMapper implements ExceptionMapper<SensorUnavailableException> {

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(SensorUnavailableException exception) {
        ApiError error = new ApiError(
                403,
                "Forbidden",
                exception.getMessage(),
                uriInfo != null ? uriInfo.getPath() : ""
        );

        return Response.status(Response.Status.FORBIDDEN)
                .entity(error)
                .build();
    }
}