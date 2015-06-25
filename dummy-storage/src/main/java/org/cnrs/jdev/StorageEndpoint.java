package org.cnrs.jdev;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("storage")
public class StorageEndpoint {

	@GET
	@Path("{contentId}")
	public Response getIt(@PathParam("contentId") String contentId) {
		return Response.ok(new StreamingOutput() {

			@Override
			public void write(OutputStream output) throws IOException,
					WebApplicationException {
				output.write("salut".getBytes());

			}
		}, MediaType.APPLICATION_OCTET_STREAM).build();
	}

	@POST
	@Path("{contentId}/{resolution}")
	public Response postId(@PathParam("resolution") String resolution,
			InputStream is) {

		return Response.ok().build();

	}
}
