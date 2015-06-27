package com.enseirb.telecom.dngroup.dvd2c.service.impl;

import java.io.File;
import java.io.IOException;

import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.enseirb.telecom.dngroup.dvd2c.service.FileTypeEnum;
import com.enseirb.telecom.dngroup.dvd2c.service.FileTypeResolverFacade;

@Service
public class TikaFacadeImpl implements FileTypeResolverFacade {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(FileTypeResolverFacade.class);

	private final static Tika tika = new Tika();

	@Override
	public synchronized FileTypeEnum detect(File tempFile) {

		try {
			String type = tika.detect(tempFile);
			if (type.startsWith("video")) {
				return FileTypeEnum.VIDEO;
			} else
				return FileTypeEnum.CLOUD;
		} catch (IOException e) {
			LOGGER.warn("failed to detect file type {}, will be used as cloud",
					tempFile.toString());
			return FileTypeEnum.CLOUD;
		}
	}
}
