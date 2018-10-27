package iot.zjt.jclient.util;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpUtil {

    private static final CloseableHttpClient httpClient = HttpClients.createDefault();

    private HttpUtil() {

    }

    public static final CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    public static final String doGet(URI uri) {
        CloseableHttpResponse response = null;
        String res = null;
        HttpGet httpGet = new HttpGet(uri);
        
        try {
            response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                res = EntityUtils.toString(response.getEntity(), "utf-8");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return res;
    }

    public static final String doPost(URI uri, String jsonParam) {
        String res = null;
        CloseableHttpResponse response = null;
        List<BasicNameValuePair> paramPairs = new ArrayList<>();
        paramPairs.add(new BasicNameValuePair("json", jsonParam));

        try {
            HttpPost httpPost = new HttpPost(uri);
            UrlEncodedFormEntity postEntity = new UrlEncodedFormEntity(paramPairs);
            postEntity.setContentType("application/x-www-form-urlencoded");
            postEntity.setContentEncoding("utf-8");
            httpPost.setEntity(postEntity);

            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                res = EntityUtils.toString(response.getEntity(), "utf-8");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return res;
    }

}
