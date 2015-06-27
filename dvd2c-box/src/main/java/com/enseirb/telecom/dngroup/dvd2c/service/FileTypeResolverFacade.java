package com.enseirb.telecom.dngroup.dvd2c.service;

import java.io.File;

public interface FileTypeResolverFacade {

	FileTypeEnum  detect(File tempFile);

}
