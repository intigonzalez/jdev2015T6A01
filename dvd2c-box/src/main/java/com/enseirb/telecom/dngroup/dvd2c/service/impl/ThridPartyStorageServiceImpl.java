package com.enseirb.telecom.dngroup.dvd2c.service.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.core.UriBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.enseirb.telecom.dngroup.dvd2c.modeldb.Document;
import com.enseirb.telecom.dngroup.dvd2c.modeldb.ThirdPartyConfiguration;
import com.enseirb.telecom.dngroup.dvd2c.repository.DocumentRepository;
import com.enseirb.telecom.dngroup.dvd2c.repository.ThirdPartyStorageConfigRepository;
import com.enseirb.telecom.dngroup.dvd2c.service.ThridPartyStorageService;

@Service
public class ThridPartyStorageServiceImpl implements ThridPartyStorageService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ThridPartyStorageServiceImpl.class);

	@Inject
	DocumentRepository docRepo;

	@Inject
	ThirdPartyStorageConfigRepository repo;

	private List<URI> generateRedirectUri(Document doc) {
		List<URI> res = new ArrayList<URI>();
		for (ThirdPartyConfiguration conf : repo.findAll()) {
			if (thirdPartyDeployable(conf, doc.getType())) {
				res.add(UriBuilder.fromPath(conf.getBaseUrl())
						.path("" + doc.getId()).build());
			}
		}

		return res;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<URI> generateRedirectUri(String contentId) {
		Document doc = docRepo.findOne(Integer.valueOf(contentId));
		if (doc != null) {
			return generateRedirectUri(doc);
		} else
			return Collections.EMPTY_LIST;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enseirb.telecom.dngroup.dvd2c.service.ThridPartyStorage#register(
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void register(String baseUrL, String name) {
		if (repo.findByBaseUrl(baseUrL) == null) {
			ThirdPartyConfiguration conf = new ThirdPartyConfiguration();
			conf.setBaseUrl(baseUrL);
			conf.setName(name);
			repo.save(conf);
		} else {
			LOGGER.debug("third party already registered");
		}

	}

	private boolean thirdPartyDeployable(ThirdPartyConfiguration conf,
			String type) {
		return true;
	}

}
