package de.dpunkt.myaktion.monitor.util;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class DisableHostnameVerifier implements HostnameVerifier {
    public boolean verify(String hostname, SSLSession sslSession) {
	return true;
    }
}