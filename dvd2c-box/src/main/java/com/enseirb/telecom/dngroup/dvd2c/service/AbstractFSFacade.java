package com.enseirb.telecom.dngroup.dvd2c.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enseirb.telecom.dngroup.dvd2c.model.Content;
import com.enseirb.telecom.dngroup.dvd2c.model.Resolution;
import com.enseirb.telecom.dngroup.dvd2c.modeldb.Document;
import com.enseirb.telecom.dngroup.dvd2c.modeldb.DocumentAlternative;
import com.google.common.base.Throwables;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;

public abstract class AbstractFSFacade implements FSFacade {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AbstractFSFacade.class);

	public abstract String getFSRoot();

	public abstract String getOriginalName();

	private static void deleteFolder(File folder) {
		File[] files = folder.listFiles();
		if (files != null) { // some JVMs return null for empty dirs
			for (File f : files) {
				if (f.isDirectory()) {
					deleteFolder(f);
				} else {
					f.delete();
				}
			}
		}
		folder.delete();
	}

	@Override
	public File dumpToTempFile(final String string,
			final InputStream uploadedInputStream) {

		try {
			final File res = File.createTempFile(string, null);
			final OutputStream os = new FileOutputStream(res);
			ByteStreams.copy(uploadedInputStream, os);
			os.close();
			return res;
		} catch (IOException e) {
			LOGGER.error("failed to copy incoming stream to temp file");
			throw Throwables.propagate(e);
		}

	}

	@Override
	public void placeContentInTargetFolder(Content content, File tempFile) {

		Path target = Paths.get(this.getFSRoot(), content.getLink(),
				this.getOriginalName());
		try {
			Files.createParentDirs(target.toFile());
			Files.move(tempFile, target.toFile());
		} catch (IOException e) {
			LOGGER.error("failed to place file in target folder");
		}

	}

	public void deleteDocumentOnFS(Document document) {

		deleteFolder(Paths
				.get(this.getFSRoot(), document.toContent().getLink()).toFile());

	}

	public AbstractFSFacade() {
		super();
	}

	@Override
	public void saveNewResolution(final DocumentAlternative alt,
			final InputStream iS) {

		try {
			final Path path = Paths.get(this.getFSRoot(), alt.getDocument()
					.getFileLink(), alt.getResolution());
			Files.createParentDirs(path.toFile());
			final FileOutputStream fos = new FileOutputStream(path.toFile());
			ByteStreams.copy(iS, fos);
			fos.close();

		} catch (IOException e) {
			LOGGER.error(
					"failed to create new resolution on the file system {}/{}",
					alt.getDocument().getId(), alt.getResolution());
			throw Throwables.propagate(e);
		}

	}

	@Override
	public FileInputStream getContentStream(Content content, Resolution res)
			throws FileNotFoundException {

		try {
			final Path path = Paths.get(this.getFSRoot(), content.getLink(),
					res.getName());
			return new FileInputStream(path.toFile());
		} catch (FileNotFoundException e) {
			LOGGER.warn("failed to get stream from file in the database");
			throw e;
		}

	}

	public InputStream getContentStream(Content content)
			throws FileNotFoundException {
		try {
			final Path path = Paths.get(this.getFSRoot(), content.getLink(),
					this.getOriginalName());
			return new FileInputStream(path.toFile());
		} catch (FileNotFoundException e) {
			LOGGER.warn("failed to get stream from file in the database");
			throw e;
		}
	}

}