package com.enseirb.telecom.s9;

import java.util.Properties;

public abstract class ApplicationContext {
	static Properties properties = new Properties();

	public static Properties getProperties() {
		return properties;
	}

	public static void setProperties(Properties properties) {
		ApplicationContext.properties = properties;
	}
}
