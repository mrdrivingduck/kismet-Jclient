package iot.zjt.jclient.util;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class HttpUtil {

    private static final CloseableHttpClient httpClient = HttpClients.createDefault();

    private HttpUtil() {

    }

    public static final CloseableHttpClient getHttpClient() {
        return httpClient;
    }

}
