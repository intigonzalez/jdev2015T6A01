package org.cnrs.jdev;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("storage")
public class StorageEndpoint {

	DataStoreService dataStoreService = new DataStoreServiceNotification(
			new DataStoreServiceImpl());

	@GET
	@Path("{contentId}/{resolution}")
	public Response getIt(final @PathParam("contentId") String contentId,
			final @PathParam("resolution") String resolution) {
		return Response
				.ok(new StreamingOutput() {

					@Override
					public void write(OutputStream output) throws IOException,
							WebApplicationException {
						dataStoreService.get(contentId, resolution, output);

					}
				}, MediaType.APPLICATION_OCTET_STREAM)
				.header("content-disposition",
						"attachment; filename = " + resolution + ".mp4")
				.build();
	}

	@POST
	@Path("{contentId}/{resolution}")
	public Response postId(@PathParam("contentId") String contentId,
			@PathParam("resolution") String resolution, InputStream is)
			throws IOException {

		dataStoreService.put(contentId, resolution, is);
		return Response.ok().build();

	}
}
