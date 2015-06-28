package com.enseirb.telecom.dngroup.dvd2c.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.enseirb.telecom.dngroup.dvd2c.service.AbstractFSFacade;

@Service
public class FSFacadeImpl extends AbstractFSFacade {
	static final public String ORIGINAL = "original";
	static final public String VAR_WWW_HTML = "/var/www/html";

	static final Logger LOGGER = LoggerFactory.getLogger(FSFacadeImpl.class);

	@Override
	public String getFSRoot() {
		return VAR_WWW_HTML;
	}

	@Override
	public String getOriginalName() {
		return ORIGINAL;
	}
}
