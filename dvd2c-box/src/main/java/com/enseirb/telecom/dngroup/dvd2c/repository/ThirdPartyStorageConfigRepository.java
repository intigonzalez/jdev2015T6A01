package com.enseirb.telecom.dngroup.dvd2c.repository;

import org.springframework.data.repository.CrudRepository;

import com.enseirb.telecom.dngroup.dvd2c.modeldb.Document;
import com.enseirb.telecom.dngroup.dvd2c.modeldb.ThirdPartyConfiguration;

public interface ThirdPartyStorageConfigRepository extends CrudRepository<ThirdPartyConfiguration, Integer>{

	
	public ThirdPartyConfiguration findByBaseUrl(String uri);
	
}
