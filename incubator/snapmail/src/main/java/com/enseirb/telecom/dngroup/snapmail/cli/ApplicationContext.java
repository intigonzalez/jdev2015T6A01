package com.enseirb.telecom.dngroup.snapmail.cli;

import java.util.Properties;


//NH: merge with cliconf single
public abstract class ApplicationContext {
	public static Properties properties = new Properties();

	public static Properties getProperties() {
		return properties;
	}

	public static void setProperties(Properties properties) {
		ApplicationContext.properties = properties;
	}
}