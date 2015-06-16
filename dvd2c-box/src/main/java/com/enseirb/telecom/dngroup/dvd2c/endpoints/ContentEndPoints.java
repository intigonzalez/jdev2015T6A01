package com.enseirb.telecom.dngroup.dvd2c.endpoints;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import com.enseirb.telecom.dngroup.dvd2c.CliConfSingleton;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.model.Content;
import com.enseirb.telecom.dngroup.dvd2c.modeldb.User;
import com.enseirb.telecom.dngroup.dvd2c.service.AccountService;
import com.enseirb.telecom.dngroup.dvd2c.service.ContentService;
import com.enseirb.telecom.dngroup.dvd2c.service.RelationService;
import com.google.common.io.Files;

// The Java class will be hosted at the URI path "/app/content"
@Path("app/content")
public class ContentEndPoints {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ContentEndPoints.class);

	@Inject
	protected ContentService cManager;

	@Inject
	protected RelationService rManager;
	
	@Inject
	protected AccountService uManager;

	// ContentService uManager = new ContentServiceImpl(
	// new ContentRepositoryMongo(), new RabbitMQServer());

	/**
	 * Get all contents for a user. This request only called by videos owners
	 * 
	 * @param userID
	 * @return Content list
	 */
	@GET
	@RolesAllowed("other")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<Content> getAllContentsFromUser() {
		String uuid = SecurityContextHolder.getContext().getAuthentication()
				.getName();

		List<Content> contents = cManager.getAllContentsFromUser(UUID
				.fromString(uuid));
		for (Content content : contents) {
			rManager.getContentRole(content);
		}
		
		return contents;
	}

	/**
	 * Get a specific content from the owner
	 * 
	 * @param userID
	 * @return Content list
	 */
	@GET
	@Path("{contentsID}/metadata")
	@RolesAllowed({ "authenticated", "other" })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Content getContentMetadata(@PathParam("contentsID") Integer contentsID) {
		String uuid = SecurityContextHolder.getContext().getAuthentication()
				.getName();

		Content content;
		try {
			content = cManager.getContent(contentsID);
			if (content.getActorID().equals(uuid)) {
				content.setLink(CliConfSingleton.publicAddr + content.getLink());
				return content;
			} else {
				// No URL parameter idLanguage was sent
				ResponseBuilder builder = Response
						.status(Response.Status.FORBIDDEN);
				builder.entity("This content doesn't belong to you ! ");
				Response response = builder.build();
				throw new WebApplicationException(response);
			}
		} catch (NoContentException e) {
			throw new WebApplicationException(e.getLocalizedMessage(),
					Status.NO_CONTENT);
		}

	}

	/**
	 * Get a specific content from the owner
	 * 
	 * @param userID
	 * @return Content list
	 * @throws URISyntaxException
	 */
	@GET
	@Path("{contentsID}")
	@RolesAllowed({ "authenticated", "other" })
	@Produces({ MediaType.WILDCARD })
	public Response getContent(@PathParam("contentsID") Integer contentsID)
			throws URISyntaxException {
		String uuid = SecurityContextHolder.getContext().getAuthentication()
				.getName();
		Content content;
		try {
			content = cManager.getContent(contentsID);

			if (content.getActorID().equals(uuid)) {
				URI uri = new URI(CliConfSingleton.publicAddr
						+ content.getLink() + "/" + content.getName());
				return Response.seeOther(uri).build();
			} else {
				// No URL parameter idLanguage was sent
				ResponseBuilder builder = Response
						.status(Response.Status.FORBIDDEN);
				builder.entity("This content doesn't belong to you ! ");
				Response response = builder.build();
				throw new WebApplicationException(response);
			}
		} catch (NoContentException e) {
			throw new WebApplicationException(e.getLocalizedMessage(),
					Status.NO_CONTENT);
		}

	}
	@GET
	@Path("/{contentsID}/{userId}")
	@RolesAllowed({ "authenticated", "other" })
	@Produces({ MediaType.WILDCARD })
	public Content getContent(@PathParam("contentsID") Integer contentsID, @PathParam("userId") String userID)
			throws URISyntaxException {
//		String uuid = SecurityContextHolder.getContext().getAuthentication()
//				.getName();
		User user = null;
		try {
			user = uManager.findUserByEmail(userID);
		} catch (NoSuchUserException e1) {
			e1.printStackTrace();
		}
		String uuid = user.getId().toString();
		Content content;
		try {
			content = cManager.getContent(contentsID);

			if (content.getActorID().equals(uuid)) {
				URI uri = new URI(CliConfSingleton.publicAddr
						+ content.getLink() + "/" + content.getName());
				return content;
			} else {
				// No URL parameter idLanguage was sent
				ResponseBuilder builder = Response
						.status(Response.Status.FORBIDDEN);
				builder.entity("This content doesn't belong to you ! ");
				Response response = builder.build();
				throw new WebApplicationException(response);
			}
		} catch (NoContentException e) {
			throw new WebApplicationException(e.getLocalizedMessage(),
					Status.NO_CONTENT);
		}

	}
	// @POST
	// @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	// public Response postVideo(Content content) {
	// return Response.status(Status.SERVICE_UNAVAILABLE).build();
	// }

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
			throws URISyntaxException, IOException, NoSuchUserException {
		String uuid = SecurityContextHolder.getContext().getAuthentication()
				.getName();

		String fileName = fileDetail.getFileName();
//		LOGGER.info("New file {}", fileDetail);
//		String extension = Files.getFileExtension(fileName);
//		MediaType fileMediaType = body.getMediaType();
//		String fileTypeTemp = fileMediaType.toString();
//		String[] fileType = fileTypeTemp.split("/");
//
//		File upload = File.createTempFile(UUID.randomUUID().toString(), "."
//				+ extension, Files.createTempDir());
//		Content content = cManager.createContent(uuid, uploadedInputStream,
//				fileType, upload);
//		// content.setLink(CliConfSingleton.publicAddr + content.getLink());
//		// return content;
//		// return Response.created(new
//		// URI("app/content/"+content.getContentsID())).build();
//		return Response.created(
//				new URI(CliConfSingleton.publicAddr + "/api/app/content/"
//						+ content.getContentsID())).build();
		return postContent2(uploadedInputStream, fileName, uManager.findUserByUUID(UUID.fromString(uuid)).getEmail());

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
	@Path("{userID}")
	@Consumes(MediaType.WILDCARD)
	public Response postContent2(InputStream iS,
			@HeaderParam("Content-Disposition") String contentDisposition, @PathParam("userID") String userID)
			throws URISyntaxException, IOException {
//		String uuid = SecurityContextHolder.getContext().getAuthentication()
//				.getName();
		User user = null;
		try {
			user = uManager.findUserByEmail(userID);
		} catch (NoSuchUserException e1) {

			e1.printStackTrace();
		}
		UUID uuid=user.getId();
		
		LOGGER.debug("New local upload, Content-Disposition : "
				+ contentDisposition);
		try {
			Content content = cManager.createContent(uuid.toString(), iS,
					contentDisposition);
			content.setLink(CliConfSingleton.publicAddr + content.getLink());

			LOGGER.debug("Content created :" + CliConfSingleton.publicAddr
					+ "/api/app/content/" + content.getContentsID());
			return Response.created(
					new URI(CliConfSingleton.publicAddr + "/api/app/content/"
							+ content.getContentsID())).build();
		} catch (IOException | SecurityException e) {
			throw e;
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

	/**
	 * Update information for the video
	 * 
	 * @param content
	 *            the content
	 * @param contentsID
	 *            the id of the content
	 * @return
	 */
	@PUT
	@RolesAllowed({ "authenticated", "other" })
	@Path("{contentsID}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response putContent(Content content,
			@PathParam("contentsID") Integer contentsID) {
		String uuid = SecurityContextHolder.getContext().getAuthentication()
				.getName();
		if (!content.getActorID().equals(uuid)) {
			return Response.status(Status.FORBIDDEN).build();
		}

		// TODO: need to check the authentication of the user
		content.setContentsID(contentsID);
		// modify the content
		if (cManager.contentExist(content.getContentsID()) == true) {
			cManager.saveContent(content);
			rManager.setContentRole(content);
			return Response.status(200).build();
		} else {
			return Response.status(409).build();
		}

	}

	/**
	 * delete the contents with contentsID
	 * 
	 * @param contentsID
	 *            the contentsID to delete
	 * @return
	 */
	@DELETE
	@RolesAllowed("other")
	@Path("{contentsID}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deleteContent(@PathParam("contentsID") Integer contentsID) {
		String uuid = SecurityContextHolder.getContext().getAuthentication()
				.getName();

		try {
			Content contents = cManager.getContent(contentsID);
			if (contents.getActorID().equals(uuid)) {
				cManager.deleteContent(contentsID);
				return Response.status(200).build();
			} else
				throw new WebApplicationException(Status.FORBIDDEN);
		} catch (NoContentException e) {
			throw new WebApplicationException(e.getLocalizedMessage(),
					Status.NO_CONTENT);
		}

	}

}
