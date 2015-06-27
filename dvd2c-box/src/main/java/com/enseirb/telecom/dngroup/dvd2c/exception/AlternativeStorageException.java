package com.enseirb.telecom.dngroup.dvd2c.exception;

import java.net.URI;

public class AlternativeStorageException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1233915278253970239L;
	URI uri;

	public URI getUri() {
		return uri;
	}

	public void setUri(URI uri) {
		this.uri = uri;
	}

}
