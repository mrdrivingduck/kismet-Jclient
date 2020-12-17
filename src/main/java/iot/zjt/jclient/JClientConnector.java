package iot.zjt.jclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import io.vertx.core.Vertx;
import iot.zjt.jclient.message.AlertMessage;
import iot.zjt.jclient.message.BSSIDMessage;
import iot.zjt.jclient.message.ClientMessage;
import iot.zjt.jclient.message.KismetMessage;
import iot.zjt.jclient.message.MsgMessage;
import iot.zjt.jclient.message.TimeMessage;
import iot.zjt.jclient.util.HttpRequestBuilder;
import iot.zjt.jclient.util.JsonParamBuilder;
import iot.zjt.jclient.util.MsgBuilder;
import iot.zjt.jclient.util.UriGenerator;

/**
 * The core connector of JClient
 * @version 2018.11.16
 * @author Mr Dk.
 */

public class JClientConnector {

    private static long timestamp = 0L;

    private final String host;
    private final int port;

    private boolean running;
    private long timerID;

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
    public JClientConnector(String host, int port, final Vertx vertx, long period) {
        this.host = host;
        this.port = port;
        this.running = true;
        this.allListeners = new ArrayList<>();
        this.allSubscriptions = new HashSet<>();

        this.timerID = vertx.setPeriodic(period, handler -> {

            if (running == false) {
                generateTerminateMessage();

                try {
                    HttpRequestBuilder.getHttpClient().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                vertx.cancelTimer(this.timerID);
                return;
            }
            
            if (allSubscriptions.contains(MsgMessage.class)) {
                try {
                    generateMsgMessage(timestamp);
                } catch (IOException e) {
                    printStackTrace();
                }
                
            }

            if (allSubscriptions.contains(BSSIDMessage.class)) {
                try {
                    generateBSSIDMessage(timestamp);
                } catch (IOException e) {
                    printStackTrace();
                }
            }

            if (allSubscriptions.contains(ClientMessage.class)) {
                try {
                    generateClientMessage(timestamp);
                } catch (IOException e) {
                    printStackTrace();
                }
            }

            if (allSubscriptions.contains(AlertMessage.class)) {
                try {
                    generateAlertMessage(timestamp);
                } catch (IOException e) {
                    printStackTrace();    
                }
            }

            try {
                timestamp = generateTimeMessage();
            } catch (IOException e) {
                printStackTrace();
            }
        });

    }

    /**
     * To print connection failure message
     */
    private void printStackTrace() {
        System.err.println("[ERROR] JClientConnector: Connection refused by - " + this.host + ":" + this.port);
        System.err.println("    Make sure kismet server is running at - " + this.host + ":" + this.port);
    }

    /**
     * To generate an alert message
     * @param timestamp
     * @throws IOException
     */
    private void generateAlertMessage(long timestamp) throws IOException {
        String res = HttpRequestBuilder.doGet(
            UriGenerator.buildUri(host, port, AlertMessage.class, timestamp)
        );
        JSONObject alertList = JSON.parseObject(res);
        JSONArray alertArray = alertList.getJSONArray("kismet.alert.list");
        publishMsg(alertArray, AlertMessage.class);
    }

    /**
     * To generate a Wi-Fi client message
     * @param timestamp
     * @throws IOException
     */
    private void generateClientMessage(long timestamp) throws IOException {
        JsonParamBuilder paramBuilder = new JsonParamBuilder();
        paramBuilder.addFields(ClientMessage.class);
        paramBuilder.addRegex("kismet.device.base.phyname", "^IEEE802.11$");
        paramBuilder.addRegex("kismet.device.base.type", "^WiFi Client$");
        String param = paramBuilder.build();

        String res = HttpRequestBuilder.doPost(
            UriGenerator.buildUri(host, port, ClientMessage.class, timestamp), param
        );
        
        JSONArray clientArray = JSON.parseArray(res);
        publishMsg(clientArray, ClientMessage.class);
    }

    /**
     * To generate a Wi-Fi AP Message
     * @param timestamp
     * @throws IOException
     */
    private void generateBSSIDMessage(long timestamp) throws IOException {
        JsonParamBuilder paramBuilder = new JsonParamBuilder();
        paramBuilder.addFields(BSSIDMessage.class);
        paramBuilder.addRegex("kismet.device.base.phyname", "^IEEE802.11$");
        paramBuilder.addRegex("kismet.device.base.type", "^WiFi AP$");
        String param = paramBuilder.build();

        String res = HttpRequestBuilder.doPost(
            UriGenerator.buildUri(host, port, BSSIDMessage.class, timestamp), param
        );
        
        JSONArray apArray = JSON.parseArray(res);
        publishMsg(apArray, BSSIDMessage.class);
    }

    /**
     * To generate a system message
     * @param timestamp
     * @throws IOException
     */
    private void generateMsgMessage(long timestamp) throws IOException {
        String res = HttpRequestBuilder.doGet(
            UriGenerator.buildUri(host, port, MsgMessage.class, timestamp)
        );
        JSONObject msgList = JSON.parseObject(res);
        JSONArray msgArray = msgList.getJSONArray("kismet.messagebus.list");
        publishMsg(msgArray, MsgMessage.class);
    }

    /**
     * To generate a timestamp message
     * @return
     * @throws IOException
     */
    private long generateTimeMessage() throws IOException {
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
