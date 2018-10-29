package iot.zjt.jclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import iot.zjt.jclient.information.KismetInfo;
import iot.zjt.jclient.information.MessageInfo;
import iot.zjt.jclient.information.TimestampInfo;
import iot.zjt.jclient.information.WiFiAPInfo;
import iot.zjt.jclient.util.HttpRequestBuilder;
import iot.zjt.jclient.util.InfoBuilder;
import iot.zjt.jclient.util.JsonParamBuilder;
import iot.zjt.jclient.util.UriGenerator;

/**
 * The core connector of JClient
 * @version 2018.10.29
 * @author Mr Dk.
 */

public class JClientConnector {

    private final String host;
    private final int port;

    private boolean running;

    private List<JClientListener> allListeners;
    private Set<Class<? extends KismetInfo>> allSubscriptions;

    /**
     * To register a listener on this connection,
     * updated the subscriptions of this connection
     * @param listener
     */
    public synchronized void register(JClientListener listener) {
        if (!allListeners.contains(listener)) {
            allListeners.add(listener);
            updateSubscriptions();
        }
    }

    /**
     * To unregister a listener on this connection,
     * updated the subscriptions of this connection
     * @param listener
     */
    public synchronized void unregister(JClientListener listener) {
        if (allListeners.contains(listener)) {
            allListeners.remove(listener);
            updateSubscriptions();
        }
    }

    /**
     * Updated subscriptions of all JClientListeners
     */
    public synchronized void updateSubscriptions() {
        allSubscriptions.clear();
        for (JClientListener listener : this.allListeners) {
            allSubscriptions.addAll(listener.getSubscription());
        }
    }

    /**
     * Consturctor,
     * generating a working thread
     * @param host
     * @param port
     */
    public JClientConnector(String host, int port) {
        this.host = host;
        this.port = port;
        this.running = true;
        this.allListeners = new ArrayList<>();
        this.allSubscriptions = new HashSet<>();

        new Thread() {
        	
        	@Override
        	public void run() {

                long timestamp = 0L;

                while (running) {

                    if (allSubscriptions.contains(MessageInfo.class)) {
                        generateMessageInfo(timestamp);
                    }

                    if (allSubscriptions.contains(WiFiAPInfo.class)) {
                        generateWiFiAPInfo(timestamp);
                    }
        
                    timestamp = generateTimestampInfo();

                    try {
                        sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                
                generateTerminateInfo();

                try {
                    HttpRequestBuilder.getHttpClient().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        	}
        }.start();
    }

    private void generateWiFiAPInfo(long timestamp) {
        JsonParamBuilder paramBuilder = new JsonParamBuilder();
        paramBuilder.addFields(WiFiAPInfo.class);
        paramBuilder.addRegex("kismet.device.base.phyname", "^IEEE802.11$");
        paramBuilder.addRegex("kismet.device.base.type", "^WiFi AP$");
        String param = paramBuilder.build();

        String res = HttpRequestBuilder.doPost(
            UriGenerator.buildUri(host, port, WiFiAPInfo.class, timestamp), param
        );
        
        JSONArray wifiApArray = JSON.parseArray(res);
        publishInfo(wifiApArray, WiFiAPInfo.class);
    }

    /**
     * To generate a MessageInfo
     * @param timestamp
     */
    private void generateMessageInfo(long timestamp) {
        String res = HttpRequestBuilder.doGet(
            UriGenerator.buildUri(host, port, MessageInfo.class, timestamp)
        );
        JSONObject msgList = JSON.parseObject(res);
        JSONArray msgArray = msgList.getJSONArray("kismet.messagebus.list");
        publishInfo(msgArray, MessageInfo.class);
    }

    /**
     * To generate a TimestampInfo
     * @return The timestamp of Kismet server
     */
    private long generateTimestampInfo() {
        String res = HttpRequestBuilder.doGet(
            UriGenerator.buildUri(host, port, TimestampInfo.class)
        );
        JSONObject timestampObj = JSON.parseObject(res);
        KismetInfo info = publishInfo(timestampObj, TimestampInfo.class);
        return ((TimestampInfo) info).getSec();
    }

    /**
     * Build and publish a KismetInfo to all subscribed listeners
     * @param obj
     * @param clazz
     * @return A built KismetInfo
     */
    private KismetInfo publishInfo(JSONObject obj, Class<? extends KismetInfo> clazz) {
        synchronized(allListeners) {
            for (JClientListener listener : allListeners) {
                if (listener.getSubscription().contains(clazz)) {
                    listener.onInformation(InfoBuilder.buildInfo(obj, clazz));
                }
            }
        }
        return InfoBuilder.buildInfo(obj, clazz);
    }

    /**
     * Build and publish an array of KismetInfo(s) to all subscribed listeners
     * @param arr
     * @param clazz
     */
    private void publishInfo(JSONArray arr, Class<? extends KismetInfo> clazz) {
        for (int i = 0; i < arr.size(); i++) {
            publishInfo(arr.getJSONObject(i), clazz);
        }
    }

    /**
     * Generate a terminate information to all listeners
     */
    private void generateTerminateInfo() {
        synchronized(allListeners) {
            for (JClientListener listener : allListeners) {
                listener.onTerminate("Connector killed");
            }
        }
    }

    /**
     * Kill the working thread
     */
    public synchronized void Kill() {
        running = false;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
