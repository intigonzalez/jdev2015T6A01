package com.enseirb.telecom.dngroup.dvd2c.endpoints;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.annotation.XmlRootElement;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enseirb.telecom.dngroup.dvd2c.CliConfSingleton;
import com.enseirb.telecom.dngroup.dvd2c.exception.AlternativeStorageException;
import com.enseirb.telecom.dngroup.dvd2c.model.Content;
import com.enseirb.telecom.dngroup.dvd2c.model.Resolution;
import com.enseirb.telecom.dngroup.dvd2c.service.ContentService;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;

@Path("content")
public class ContentEndPoints {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ContentEndPoints.class);

	@Inject
	ContentService contentService;

	@GET
	@Path("{contentsID}/metadata")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Content getContentMetadata(
			@PathParam("contentsID") Integer contentsID)
			throws URISyntaxException {

		try {
			return contentService.getContent(contentsID);
		} catch (NoContentException e) {
			throw new WebApplicationException(404);
		}

	}

	@GET
	@Path("{contentsID}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getContent(@PathParam("contentsID") Integer contentsID)
			throws URISyntaxException {

		Content content;
		try {
			content = contentService.getContent(contentsID);
			File original = new File("/var/www/html" + content.getLink()
					+ "/original");

			final FileInputStream fis = new FileInputStream(original);

			return Response
					.ok(new StreamingOutput() {

						@Override
						public void write(OutputStream output)
								throws IOException, WebApplicationException {
							ByteStreams.copy(fis, output);
							fis.close();

						}
					}, MediaType.APPLICATION_OCTET_STREAM_TYPE)
					.header("content-disposition",
							"attachment; filename = original.mp4").build();

		} catch (NoContentException e) {
			throw new WebApplicationException(e.getLocalizedMessage(),
					Status.NO_CONTENT);
		} catch (FileNotFoundException e) {
			throw new WebApplicationException(e.getLocalizedMessage(),
					Status.NOT_FOUND);
		}

	}

	@SuppressWarnings("resource")
	@GET
	@Path("{contentsID}/{resolutionName}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getContent(
			final @PathParam("contentsID") Integer contentsID,
			final @PathParam("resolutionName") String resolutionName)
			throws URISyntaxException, IOException {

		FileInputStream fis;
		try {

			fis = contentService.getContentStream(contentsID, resolutionName);
			return Response
					.ok(new StreamingOutput() {

						@Override
						public void write(OutputStream output)
								throws IOException, WebApplicationException {
							ByteStreams.copy(fis, output);
							// fis.close();

						}
					}, MediaType.APPLICATION_OCTET_STREAM)
					.header("content-disposition",
							"attachment; filename = " + resolutionName + ".mp4")
					.build();

		} catch (AlternativeStorageException ase) {
			return Response.temporaryRedirect(ase.getUri()).build();
		}

	}

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("text/plain")
	public Response postFromForm(
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail,
			@FormDataParam("file") FormDataBodyPart body)
			throws URISyntaxException, IOException {

		String fileName = fileDetail.getFileName();

		return rawPost(uploadedInputStream, fileName);

	}

	@POST
	@Consumes(MediaType.WILDCARD)
	@Produces("text/plain")
	public Response rawPost(InputStream iS,
			@HeaderParam("Content-Disposition") String contentDisposition)
			throws URISyntaxException, IOException {
		String uuid = UUID.randomUUID().toString();

		try {
			Content content = contentService.createContent(uuid, iS,
					contentDisposition);
			content.setLink(CliConfSingleton.publicAddr + content.getLink());

			return Response
					.created(
							new URI(CliConfSingleton.getBaseApiURI()
									+ "/content/" + content.getContentsID()))
					.entity(new URI(CliConfSingleton.getBaseApiURI()
							+ "/content/" + content.getContentsID()).toString())
					.build();
		} catch (IOException | SecurityException e) {
			throw e;
		}
	}

	@XmlRootElement
	class MyURL {
		public String url;
	}

	@PUT
	@Path("{contentId}/{resolution}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response receiveNewURI(@PathParam("contentId") String contentId,
			@PathParam("resolution") String resolution, String url) {
		Gson gson = new Gson();
		MyURL myurl = gson.fromJson(url, MyURL.class);
		try {
			contentService.updateContentWithUrl(contentId, resolution,
					myurl.url);
			return Response.accepted().build();
		} catch (NoContentException e) {
			throw new WebApplicationException(404);
		}

	}

	@POST
	@Path("{contentId}/{resolution}")
	@Consumes(MediaType.WILDCARD)
	public Response postNewResolution(@PathParam("contentId") String contentId,
			@PathParam("resolution") String resolution, InputStream iS,
			@HeaderParam("Content-Disposition") String contentDisposition)
			throws URISyntaxException, IOException {

		try {
			Content content = contentService.createNewContentResolution(
					contentId, resolution, iS, contentDisposition);
			LOGGER.debug("new content resolution created {} (total:{})",
					content.getContentsID(), content.getResolution().size());
		} catch (AlternativeStorageException e) {
			return Response.temporaryRedirect(e.getUri()).build();
		}

		return Response.noContent().build();

	}

}
