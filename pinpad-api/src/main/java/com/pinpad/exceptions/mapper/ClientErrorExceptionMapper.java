package com.pinpad.exceptions.mapper;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.pinpad.exceptions.ApiErrorResponse;

@Provider
public class ClientErrorExceptionMapper implements ExceptionMapper<ClientErrorException> {

	@Override
	public Response toResponse(ClientErrorException exception) {
		exception.printStackTrace();
		return Response.status(exception.getResponse().getStatus())
				.entity(new ApiErrorResponse(exception.getResponse().getStatus(), false, exception.getMessage(), null))
				.type(MediaType.APPLICATION_JSON).build();

	}

}
