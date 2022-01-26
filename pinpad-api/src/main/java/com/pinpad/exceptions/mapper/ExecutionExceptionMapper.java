package com.pinpad.exceptions.mapper;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.pinpad.exceptions.ApiErrorResponse;

/**
 * Clase implementa la respuesta con Codigo 500 que representa Server error
 * (failure) (ERROR DE SERVIDOR INTERNO)
 */
@Provider
public class ExecutionExceptionMapper implements ExceptionMapper<Throwable> {

	@Context
	private HttpHeaders headers;

	@Override
	public Response toResponse(Throwable e) {
		List<String> lsErrorData = new ArrayList<String>();
		lsErrorData.add(e.getMessage());

		StringWriter strWrtErrores = new StringWriter();
		e.printStackTrace(new PrintWriter(strWrtErrores));
		lsErrorData.add(strWrtErrores.toString());

		return Response.status(Status.INTERNAL_SERVER_ERROR)
				.entity(new ApiErrorResponse(Status.INTERNAL_SERVER_ERROR.getStatusCode(), false,
						"Ha ocurrido un error inesperado", lsErrorData))
				.type(MediaType.APPLICATION_JSON).build();
	}

}