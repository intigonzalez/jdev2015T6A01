/* 
Copyright 2013 Phil Varner

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License. 
 */

package com.philvarner.clamavj;

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ClamScan {

	private static Log log = LogFactory.getLog(ClamScan.class);

	private static final int DEFAULT_CHUNK_SIZE = 2048;
	private static final byte[] INSTREAM = "zINSTREAM\0".getBytes();

	private int timeout;
	private String host;
	private int port;

	public ClamScan() {
	}

	public ClamScan(String host, int port, int timeout) {
		setHost(host);
		setPort(port);
		setTimeout(timeout);
	}

	public ScanResult scan(byte[] in) throws IOException {
		return scan(new ByteArrayInputStream(in));
	}

	public ScanResult scan(InputStream in) throws IOException {

		Socket socket = new Socket();
		socket.connect(new InetSocketAddress(getHost(), getPort()));

		try {
			socket.setSoTimeout(getTimeout());
		} catch (SocketException e) {
			log.error("Could not set socket timeout to " + getTimeout() + "ms",
					e);
		}

		DataOutputStream dos = null;
		String response = "";
		try {
			dos = new DataOutputStream(socket.getOutputStream());
			dos.write(INSTREAM);

			int read;
			byte[] buffer = new byte[DEFAULT_CHUNK_SIZE];
			while ((read = in.read(buffer)) > 0) {
				dos.writeInt(read);
				dos.write(buffer, 0, read);
			}

			dos.writeInt(0);
			dos.flush();

			read = socket.getInputStream().read(buffer);
			if (read > 0)
				response = new String(buffer, 0, read);

		} finally {
			// if (dos != null)
			try {
				dos.close();
			} catch (IOException e) {
				log.debug("exception closing DOS", e);
			}
			try {
				socket.close();
			} catch (IOException e) {
				log.debug("exception closing socket", e);
			}
		}

		if (log.isDebugEnabled())
			log.debug("Response: " + response);

		return new ScanResult(response.trim());
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

}