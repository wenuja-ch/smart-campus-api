package com.smartcampus.mapper;

import com.smartcampus.exception.ResourceNotFoundException;
import com.smartcampus.model.ApiError;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ResourceNotFoundExceptionMapper implements ExceptionMapper<ResourceNotFoundException> {

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(ResourceNotFoundException exception) {
        ApiError error = new ApiError(
                404,
                "Not Found",
                exception.getMessage(),
                uriInfo != null ? uriInfo.getPath() : ""
        );

        return Response.status(Response.Status.NOT_FOUND)
                .entity(error)
                .build();
    }
}