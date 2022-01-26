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
import com.pinpad.exceptions.CustomExceptionHandler;

/**
 * Clase implementa la respuesta con Codigo 400 que representa Bad Request
 * (Peticion incorrecta)
 */
@Provider
public class ValidationExceptionMapper implements ExceptionMapper<CustomExceptionHandler> {

	@Context
	private HttpHeaders headers;

	@Override
	public Response toResponse(CustomExceptionHandler e) {
		List<String> lsErrorData = new ArrayList<String>();
		if (e.getData() == null) {
			StringWriter strWrtErrores = new StringWriter();
			e.printStackTrace(new PrintWriter(strWrtErrores));
			lsErrorData.add(strWrtErrores.toString());
		} else if (e.getData() instanceof String) {
			lsErrorData.add(e.getData().toString());
		} else if (e.getData() instanceof List) {
			List<?> lsObject = (List<?>) e.getData();
			lsObject.forEach(x -> {
				if (x instanceof String) {
					lsErrorData.add(x.toString());
				}
			});
		}
		if (lsErrorData.isEmpty() || lsErrorData.size() <= 0) {
			lsErrorData.add("undefined data object");
		}

		return Response.status(Status.BAD_REQUEST)
				.entity(new ApiErrorResponse(Status.BAD_REQUEST.getStatusCode(), false, e.getMessage(), lsErrorData))
				.type(MediaType.APPLICATION_JSON).build();
	}

}