package com.enseirb.telecom.dngroup.dvd2c;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.enseirb.telecom.dngroup.dvd2c.model.Content;
import com.google.common.hash.Hashing;
import com.google.common.io.ByteStreams;

// this annotation indicate that the test will only be run in an integration-test phase
@Category(IntegrationTest.class)
public class FullStackIntegrationTest {

        //we use regular junit tests, asserts...
	@Test
	public void longRunningServiceTest() throws Exception {

                //create a REST Client
		Client client = ClientBuilder.newClient();

		// we store local resources in the test folder. We have our input and output for
		// which we will compute a hash and compare with the expected result
		
		// that's our input
		URL originalURL = getClass().getResource("/marseille.3gp");
		//that's the output
		URL mediumURL = getClass().getResource("/medium.mp4");
		
		
		//we now compute the hashes
		Path originalPath = Paths.get(originalURL.toURI());
		Path mediumPath = Paths.get(mediumURL.toURI());
		String originalSha1 = com.google.common.io.Files.hash(
				originalPath.toFile(), Hashing.sha1()).toString();

		String mediumSha1 = com.google.common.io.Files.hash(
				mediumPath.toFile(), Hashing.sha1()).toString();


                //send the original file to the server
		FileInputStream fis = new FileInputStream(originalPath.toFile());

		Response res = client.target("http://localhost:8081/api/content")
				.request()
				.post(Entity.entity(fis, MediaType.APPLICATION_OCTET_STREAM));

		fis.close();
		
		//check if the response is correct
		Assert.assertEquals(201, res.getStatus());
		URI location = UriBuilder.fromUri(res.getHeaderString("Location"))
				.build();

		Thread.sleep(2000);
		
		//retrieve an input stream to the original file
		{
			InputStream is = client.target(location).request()
					.accept(MediaType.APPLICATION_OCTET_STREAM)
					.get(InputStream.class);

			Path tempFile = Files.createTempFile(null, null);
			FileOutputStream fos = new FileOutputStream(tempFile.toFile());
			ByteStreams.copy(is, fos);
			fos.flush();
			fos.close();
			Assert.assertEquals(
					originalSha1,
					com.google.common.io.Files.hash(tempFile.toFile(),
							Hashing.sha1()).toString());
		}

		
		//wait for the file to be transcoded
		Thread.sleep(20000);
		Content content = client
				.target(UriBuilder.fromUri(location).path("metadata"))
				.request().accept(MediaType.APPLICATION_XML)
				.get(Content.class);

		// check if we have the 3 resolutions
		Assert.assertEquals(3, content.getResolution().size());

                // retrieve the medium resolution and compare it to the previously hashed one
		{
			InputStream is = client
					.target(UriBuilder.fromUri(location).path("medium"))
					.request().accept(MediaType.APPLICATION_OCTET_STREAM)
					.get(InputStream.class);

			Path tempFile = Files.createTempFile(null, null);
			FileOutputStream fos = new FileOutputStream(tempFile.toFile());
			ByteStreams.copy(is, fos);
			fos.flush();
			fos.close();
			Assert.assertEquals(
					mediumSha1,
					com.google.common.io.Files.hash(tempFile.toFile(),
							Hashing.sha1()).toString());
		}

	}
}
