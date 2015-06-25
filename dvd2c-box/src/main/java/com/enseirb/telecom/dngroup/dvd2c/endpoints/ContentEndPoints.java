package com.enseirb.telecom.dngroup.dvd2c.endpoints;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enseirb.telecom.dngroup.dvd2c.CliConfSingleton;
import com.enseirb.telecom.dngroup.dvd2c.model.Content;
import com.enseirb.telecom.dngroup.dvd2c.service.ContentService;
import com.enseirb.telecom.dngroup.dvd2c.service.ThridPartyStorageService;
import com.google.common.io.ByteStreams;

// The Java class will be hosted at the URI path "/app/content"
@Path("content")
public class ContentEndPoints {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ContentEndPoints.class);

	@Inject
	protected ContentService cManager;




	

	/**
	 * Get a specific content from the owner
	 * 
	 * @param userID
	 * @return Content list
	 * @throws URISyntaxException
	 */
	@GET
	@Path("{contentsID}")
	@Produces({ MediaType.WILDCARD })
	public Response getContent(@PathParam("contentsID") Integer contentsID)
			throws URISyntaxException {

		Content content;
		try {
			content = cManager.getContent(contentsID);
			File original = new File("/var/www/html" + content.getLink()
					+ "/original");

			final FileInputStream fis = new FileInputStream(original);

			return Response.ok(new StreamingOutput() {

				@Override
				public void write(OutputStream output) throws IOException,
						WebApplicationException {
					ByteStreams.copy(fis, output);
					fis.close();

				}
			}, MediaType.APPLICATION_OCTET_STREAM_TYPE).build();

		} catch (NoContentException e) {
			throw new WebApplicationException(e.getLocalizedMessage(),
					Status.NO_CONTENT);
		} catch (FileNotFoundException e) {
			throw new WebApplicationException(e.getLocalizedMessage(),
					Status.NOT_FOUND);
		}

	}

	


	/**
	 * post a file on the box for the userID
	 * 
	 * @param userID
	 *            the sender of the request
	 * @param uploadedInputStream
	 * @param fileDetail
	 * @param body
	 * @return
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws NoSuchUserException
	 */
	@POST
	@RolesAllowed({ "other", "authenticated" })
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response postContent(
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail,
			@FormDataParam("file") FormDataBodyPart body)
			throws URISyntaxException, IOException {
		String uuid = "anonymous";

		String fileName = fileDetail.getFileName();
		// LOGGER.info("New file {}", fileDetail);
		// String extension = Files.getFileExtension(fileName);
		// MediaType fileMediaType = body.getMediaType();
		// String fileTypeTemp = fileMediaType.toString();
		// String[] fileType = fileTypeTemp.split("/");
		//
		// File upload = File.createTempFile(UUID.randomUUID().toString(), "."
		// + extension, Files.createTempDir());
		// Content content = cManager.createContent(uuid, uploadedInputStream,
		// fileType, upload);
		// // content.setLink(CliConfSingleton.publicAddr + content.getLink());
		// // return content;
		// // return Response.created(new
		// // URI("app/content/"+content.getContentsID())).build();
		// return Response.created(
		// new URI(CliConfSingleton.publicAddr + "/api/app/content/"
		// + content.getContentsID())).build();
		return postContent2(uploadedInputStream, fileName);

	}

	/**
	 * post a file on the box for the userID
	 * 
	 * @param userID
	 *            the sender of the request
	 * @param iS
	 * @param fileDetail
	 * @param body
	 * @return
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	@POST
	@RolesAllowed({ "other", "authenticated" })
	@Consumes(MediaType.WILDCARD)
	public Response postContent2(InputStream iS,
			@HeaderParam("Content-Disposition") String contentDisposition)
			throws URISyntaxException, IOException {
		String uuid = UUID.randomUUID().toString();

		LOGGER.debug("New local upload, Content-Disposition : "
				+ contentDisposition);
		try {
			Content content = cManager.createContent(uuid, iS,
					contentDisposition);
			content.setLink(CliConfSingleton.publicAddr + content.getLink());

			LOGGER.debug("Content created :" + CliConfSingleton.publicAddr
					+ "/api/app/content/" + content.getContentsID());
			return Response.created(
					new URI(CliConfSingleton.getBaseApiURI() + "/content/"
							+ content.getContentsID())).build();
		} catch (IOException | SecurityException e) {
			throw e;
		}
	}

	@Inject
	ThridPartyStorageService tps;

	@POST
	@Path("{contentId}/{resolution}")
	@Consumes(MediaType.WILDCARD)
	public Response postNewResolution(@PathParam("contentId") String contentId,
			@PathParam("resolution") String resolution, InputStream iS,
			@HeaderParam("Content-Disposition") String contentDisposition)
			throws URISyntaxException, IOException {

		List<URI> altUri = tps.generateRedirectURUri(contentId);
		if (altUri != null && altUri.size() > 0) {
			return Response.temporaryRedirect(
					UriBuilder.fromUri(altUri.get(0)).path(resolution).build())
					.build();

		} else {
			throw new WebApplicationException(
					"No Thirds Party Storage Provider registered", 404);
		}

	}

	// @POST
	// @Consumes(MediaType.APPLICATION_OCTET_STREAM)
	// @Produces(MediaType.TEXT_PLAIN)
	// public String postIf(InputStream is) throws IOException{
	// java.nio.file.Path path = java.nio.file.Files.createTempFile(null, null);
	// FileOutputStream fos = new FileOutputStream(path.toFile());
	// ByteStreams.copy(is, fos);
	// fos.close();
	// is.close();
	//
	// TikaConfig config = TikaConfig.getDefaultConfig();
	// Detector detector = config.getDetector();
	//
	// TikaInputStream stream = TikaInputStream.get(path.toFile());
	//
	// Metadata metadata = new Metadata();
	// metadata.add(Metadata.RESOURCE_NAME_KEY, path.toString());
	// org.apache.tika.mime.MediaType mediaType = detector.detect(stream,
	// metadata);
	//
	// return mediaType.toString() + "   --   " + path.toString();
	//
	// }

	// @GET
	// @Path("get")
	// @RolesAllowed({ "other", "authenticated" })
	// public Response getTest() {
	// // LOGGER.error("Is only from local not from {}", request);
	//
	// return Response.status(javax.ws.rs.core.Response.Status.OK).build();
	// }

	

	

	@POST
	@Path("{contentId}")
	public Response postNewVersionOfContent(
			@PathParam("contentId") String contentId, InputStream is) {

		throw new WebApplicationException(500);
	}

}
