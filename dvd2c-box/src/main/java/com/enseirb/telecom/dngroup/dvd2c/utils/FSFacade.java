package com.enseirb.telecom.dngroup.dvd2c.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.enseirb.telecom.dngroup.dvd2c.model.Content;
import com.enseirb.telecom.dngroup.dvd2c.model.Resolution;
import com.enseirb.telecom.dngroup.dvd2c.modeldb.Document;
import com.enseirb.telecom.dngroup.dvd2c.modeldb.DocumentAlternative;

public interface FSFacade {

	public abstract File dumpToTempFile(String string,
			InputStream uploadedInputStream);

	public abstract void placeContentInTargetFolder(Content content,
			File tempFile);

	public abstract void deleteDocumentOnFS(Document document);

	public abstract void saveNewResolution(DocumentAlternative alt,
			InputStream iS);

	public abstract FileInputStream getContentStream(Content content, Resolution res);

}