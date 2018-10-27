package iot.zjt.jclient.util;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.utils.URIBuilder;

import iot.zjt.jclient.annotation.ApiUrl;
import iot.zjt.jclient.information.KismetInfo;

public class UriGenerator {

    public static final URI buildUri(
        String host, int port,
        Class<? extends KismetInfo> infoType, Long timestamp) {

        String path = null;
        URI uri = null;
        try {
            path = String.format(infoType.getAnnotation(ApiUrl.class).value(), timestamp);

            URIBuilder builder = new URIBuilder();
            builder.setScheme("http");
            builder.setHost(String.format(host + ":%d", port));
            builder.setPath(path);
            uri = builder.build();

        } catch (NullPointerException | URISyntaxException e) {
            e.printStackTrace();
        }

        return uri;
    }

    public static final URI buildUri(
        String host, int port,
        Class<? extends KismetInfo> infoType) {

        return buildUri(host, port, infoType, null);
    }
}
