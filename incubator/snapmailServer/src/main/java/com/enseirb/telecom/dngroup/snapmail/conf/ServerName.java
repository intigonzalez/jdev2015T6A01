package com.enseirb.telecom.dngroup.snapmail.conf;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServerName {
	/**
	 * Get the local network name of the server
	 * 
	 * @return String nameoftheserver
	 */
	@SuppressWarnings("finally")
	public static String localAddress() {
		String add = "";
		try {
			InetAddress addr = InetAddress.getLocalHost();

			add = addr.getHostName();
		} catch (UnknownHostException e) {
			System.exit(1);
		} finally {
			return add;
		}
	}
}
