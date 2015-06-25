package com.enseirb.telecom.dngroup.dvd2c.service;

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

@Service
public class ThridPartyStorageServiceImpl implements ThridPartyStorageService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ThridPartyStorageServiceImpl.class);

	@Inject
	ThirdPartyStorageConfigRepository repo;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enseirb.telecom.dngroup.dvd2c.service.ThridPartyStorage#
	 * generateRedirectURUri(com.enseirb.telecom.dngroup.dvd2c.modeldb.Document)
	 */
	@Override
	public List<URI> generateRedirectURUri(Document doc) {
		List<URI> res = new ArrayList<URI>();
		for (ThirdPartyConfiguration conf : repo.findAll()) {
			if (thirdPartyDeployable(conf, doc.getType())) {
				res.add(UriBuilder.fromPath(conf.getBaseUrl())
						.path("" + doc.getId()).build());
			}
		}

		return res;
	}

	private boolean thirdPartyDeployable(ThirdPartyConfiguration conf,
			String type) {
		return true;
	}

	@Inject
	DocumentRepository docRepo;

	@Override
	public List<URI> generateRedirectURUri(String contentId) {
		Document doc = docRepo.findOne(Integer.valueOf(contentId));
		if (doc != null) {
			return generateRedirectURUri(doc);
		} else
			return Collections.EMPTY_LIST;
	}

}
