package com.enseirb.telecom.dngroup.dvd2c.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.core.UriBuilder;




import org.springframework.stereotype.Service;

import com.enseirb.telecom.dngroup.dvd2c.modeldb.Document;
import com.enseirb.telecom.dngroup.dvd2c.modeldb.ThirdPartyConfiguration;
import com.enseirb.telecom.dngroup.dvd2c.repository.ThirdPartyStorageConfigRepository;

@Service
public class ThridPartyStorageServiceImpl implements ThridPartyStorageService {

	@Inject
	ThirdPartyStorageConfigRepository repo;

	/* (non-Javadoc)
	 * @see com.enseirb.telecom.dngroup.dvd2c.service.ThridPartyStorage#register(java.lang.String, java.lang.String)
	 */
	@Override
	public void register(String baseUrL, String name) {
		ThirdPartyConfiguration conf = new ThirdPartyConfiguration();
		conf.setBaseUrl(baseUrL);
		conf.setName(name);
		repo.save(conf);

	}

	/* (non-Javadoc)
	 * @see com.enseirb.telecom.dngroup.dvd2c.service.ThridPartyStorage#generateRedirectURUri(com.enseirb.telecom.dngroup.dvd2c.modeldb.Document)
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

}
