package com.pinpad.exceptions.mapper;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.pinpad.exceptions.ApiErrorResponse;

/**
 * Clase implementa la respuesta con Codigo 401 que representa Unauthorized (No
 * autorizado)
 */
@Provider
public class AuthorizationExceptionMapper implements ExceptionMapper<NotAuthorizedException> {

	@Context
	private HttpHeaders headers;

	@Override
	public Response toResponse(NotAuthorizedException e) {
		List<String> lsErrorData = new ArrayList<String>();
		if (e.getChallenges() != null) {
			e.getChallenges().forEach(c -> {
				lsErrorData.add(c.toString());
			});
		}
		lsErrorData.add(e.getMessage());
		return Response.status(Status.UNAUTHORIZED)
				.entity(new ApiErrorResponse(Status.UNAUTHORIZED.getStatusCode(), false, e.getMessage(), lsErrorData))
				.type(MediaType.APPLICATION_JSON).build();
	}

}