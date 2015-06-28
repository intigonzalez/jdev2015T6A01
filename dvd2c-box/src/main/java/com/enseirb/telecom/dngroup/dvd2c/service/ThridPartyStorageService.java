package com.enseirb.telecom.dngroup.dvd2c.service;

import java.net.URI;
import java.util.List;

import com.enseirb.telecom.dngroup.dvd2c.modeldb.Document;

public interface ThridPartyStorageService {

	public abstract void register(String baseUrL, String name);

	public abstract List<URI> generateRedirectUri(String contentId);
	

}