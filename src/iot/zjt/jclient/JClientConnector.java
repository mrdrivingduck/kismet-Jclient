package iot.zjt.jclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import iot.zjt.jclient.message.KismetMessage;
import iot.zjt.jclient.message.MsgMessage;
import iot.zjt.jclient.message.TimeMessage;
import iot.zjt.jclient.message.WiFiAPMessage;
import iot.zjt.jclient.util.HttpRequestBuilder;
import iot.zjt.jclient.util.MsgBuilder;
import iot.zjt.jclient.util.JsonParamBuilder;
import iot.zjt.jclient.util.UriGenerator;

/**
 * The core connector of JClient
 * @version 2018.10.30
 * @author Mr Dk.
 */

public class JClientConnector {

    private final String host;
    private final int port;

    private boolean running;

    private List<JClientListener> allListeners;
    private Set<Class<? extends KismetMessage>> allSubscriptions;

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

                    if (allSubscriptions.contains(MsgMessage.class)) {
                        generateMsgMessage(timestamp);
                    }

                    if (allSubscriptions.contains(WiFiAPMessage.class)) {
                        generateWiFiAPMessage(timestamp);
                    }
        
                    timestamp = generateTimeMessage();

                    try {
                        sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                
                generateTerminateMessage();

                try {
                    HttpRequestBuilder.getHttpClient().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        	}
        }.start();
    }

    private void generateWiFiAPMessage(long timestamp) {
        JsonParamBuilder paramBuilder = new JsonParamBuilder();
        paramBuilder.addFields(WiFiAPMessage.class);
        paramBuilder.addRegex("kismet.device.base.phyname", "^IEEE802.11$");
        paramBuilder.addRegex("kismet.device.base.type", "^WiFi AP$");
        String param = paramBuilder.build();

        String res = HttpRequestBuilder.doPost(
            UriGenerator.buildUri(host, port, WiFiAPMessage.class, timestamp), param
        );
        
        JSONArray wifiApArray = JSON.parseArray(res);
        publishMsg(wifiApArray, WiFiAPMessage.class);
    }

    /**
     * To generate a MsgMessage
     * @param timestamp
     */
    private void generateMsgMessage(long timestamp) {
        String res = HttpRequestBuilder.doGet(
            UriGenerator.buildUri(host, port, MsgMessage.class, timestamp)
        );
        JSONObject msgList = JSON.parseObject(res);
        JSONArray msgArray = msgList.getJSONArray("kismet.messagebus.list");
        publishMsg(msgArray, MsgMessage.class);
    }

    /**
     * To generate a TimeMessage
     * @return The timestamp of Kismet server
     */
    private long generateTimeMessage() {
        String res = HttpRequestBuilder.doGet(
            UriGenerator.buildUri(host, port, TimeMessage.class)
        );
        JSONObject timestampObj = JSON.parseObject(res);
        KismetMessage msg = publishMsg(timestampObj, TimeMessage.class);
        return ((TimeMessage) msg).getSec();
    }

    /**
     * Build and publish a KismetMessage to all subscribed listeners
     * @param obj
     * @param clazz
     * @return A built KismetMessage
     */
    private KismetMessage publishMsg(JSONObject obj, Class<? extends KismetMessage> clazz) {
        synchronized(allListeners) {
            for (JClientListener listener : allListeners) {
                if (listener.getSubscription().contains(clazz)) {
                    listener.onMessage(MsgBuilder.buildMessage(obj, clazz));
                }
            }
        }
        return MsgBuilder.buildMessage(obj, clazz);
    }

    /**
     * Build and publish an array of KismetMessage(s) to all subscribed listeners
     * @param arr
     * @param clazz
     */
    private void publishMsg(JSONArray arr, Class<? extends KismetMessage> clazz) {
        for (int i = 0; i < arr.size(); i++) {
            publishMsg(arr.getJSONObject(i), clazz);
        }
    }

    /**
     * Generate a terminate message to all listeners
     */
    private void generateTerminateMessage() {
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
