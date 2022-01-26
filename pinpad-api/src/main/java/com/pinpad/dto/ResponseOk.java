/**
 * 
 */
package com.pinpad.dto;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;

import com.pinpad.ejb.exceptions.BOException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author H P
 *
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseOk implements Serializable {

	private static final long serialVersionUID = 1L;

	private int code;
	private boolean success;
	private String message;
	private Object data;
	public static String messageDataFound = "Data found";
	public static String messageDataNotFound = "Data not found";

	public ResponseOk(String message, Object data) {
		super();
		this.code = Status.OK.getStatusCode();
		this.success = true;
		this.message = message;
		this.data = data;
	}
	
	public Response responseOkTempFile(Path tempFile, String filename, String contentType) throws BOException {
        long filesize;
        try {
            filesize = Files.size(tempFile);
        } catch (IOException e) {
        	throw new BOException("pin.warn.tamanoArchivo", new Object[] { tempFile });
        }
        return Response.ok(newTempFileStreaming(tempFile), contentType)
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .header(HttpHeaders.CONTENT_LENGTH, filesize)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + filename + "\"")
                .build();
    }
	
	public StreamingOutput newTempFileStreaming(Path tempFile) {
        return output -> {
            Files.copy(tempFile, output);
            Files.deleteIfExists(tempFile);
        };
    }
}