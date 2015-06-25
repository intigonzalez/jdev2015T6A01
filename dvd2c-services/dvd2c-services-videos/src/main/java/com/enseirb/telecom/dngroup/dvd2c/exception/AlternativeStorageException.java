package com.enseirb.telecom.dngroup.dvd2c.exception;

import java.net.URI;

public class AlternativeStorageException extends Exception {

	URI uri;

	public URI getUri() {
		return uri;
	}

	public void setUri(URI uri) {
		this.uri = uri;
	}

}
