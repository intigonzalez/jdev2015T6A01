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
import java.util.Map.Entry;
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
import javax.ws.rs.ext.MessageBodyReader;
import javax.xml.bind.annotation.XmlRootElement;

import jersey.repackaged.com.google.common.collect.Collections2;

import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.jvnet.mimepull.MIMEPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enseirb.telecom.dngroup.dvd2c.CliConfSingleton;
import com.enseirb.telecom.dngroup.dvd2c.exception.AlternativeStorageException;
import com.enseirb.telecom.dngroup.dvd2c.model.Content;
import com.enseirb.telecom.dngroup.dvd2c.model.Resolution;
import com.enseirb.telecom.dngroup.dvd2c.service.ContentService;
import com.google.common.base.Predicate;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;

@Path("content")
public class ContentEndPoints {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ContentEndPoints.class);

	@Inject
	ContentService cManager;

	@GET
	@Path("{contentsID}/metadata")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Content getContentMetadata(
			@PathParam("contentsID") Integer contentsID)
			throws URISyntaxException {

		try {
			return cManager.getContent(contentsID);
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
			content = cManager.getContent(contentsID);
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

	@GET
	@Path("{contentsID}/{resolutionName}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getContent(
			final @PathParam("contentsID") Integer contentsID,
			final @PathParam("resolutionName") String resolutionName)
			throws URISyntaxException, IOException {

		Content content;
		try {
			content = cManager.getContent(contentsID);
			for (Resolution res : content.getResolution()) {
				if (res.getName().equals(resolutionName)) {
					if (res.getUri() == null
							|| res.getUri()
									.startsWith(
											CliConfSingleton.getBaseApiURI()
													.toString())) {// we
						// host
						// the
						// file
						File original = new File("/var/www/html"
								+ content.getLink() + "/" + resolutionName);
						FileInputStream fis = new FileInputStream(original);

						return Response
								.ok(new StreamingOutput() {

									@Override
									public void write(OutputStream output)
											throws IOException,
											WebApplicationException {
										ByteStreams.copy(fis, output);
										// fis.close();

									}
								}, MediaType.APPLICATION_OCTET_STREAM)
								.header("content-disposition",
										"attachment; filename = "
												+ resolutionName + ".mp4")
								.build();

					} else {
						return Response.temporaryRedirect(
								UriBuilder.fromUri(res.getUri()).build())
								.build();
					}

				}
			}

			throw new WebApplicationException(404);

		} catch (NoContentException e) {
			throw new WebApplicationException(e.getLocalizedMessage(),
					Status.NO_CONTENT);
		} catch (FileNotFoundException e) {
			throw new WebApplicationException(e.getLocalizedMessage(),
					Status.NOT_FOUND);
		}

	}

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
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
	public Response rawPost(InputStream iS,
			@HeaderParam("Content-Disposition") String contentDisposition)
			throws URISyntaxException, IOException {
		String uuid = UUID.randomUUID().toString();

		try {
			Content content = cManager.createContent(uuid, iS,
					contentDisposition);
			content.setLink(CliConfSingleton.publicAddr + content.getLink());

			return Response.created(
					new URI(CliConfSingleton.getBaseApiURI() + "/content/"
							+ content.getContentsID())).build();
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
			cManager.updateContentWithUrl(contentId, resolution, myurl.url);
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
			Content content = cManager.createNewContentResolution(contentId,
					resolution, iS, contentDisposition);
		} catch (AlternativeStorageException e) {
			return Response.temporaryRedirect(e.getUri()).build();
		}

		return Response.noContent().build();

	}

}
