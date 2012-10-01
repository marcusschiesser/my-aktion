package de.dpunkt.myaktion.monitor.util;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class LocalHostnameVerifier implements HostnameVerifier {
	public boolean verify(String hostname, SSLSession sslSession) {
		return ("localhost".equals(hostname));
	}
}