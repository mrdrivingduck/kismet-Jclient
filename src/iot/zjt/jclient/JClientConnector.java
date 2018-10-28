package iot.zjt.jclient;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import iot.zjt.jclient.information.KismetInfo;
import iot.zjt.jclient.information.TimestampInfo;
import iot.zjt.jclient.util.HttpRequestBuilder;
import iot.zjt.jclient.util.InfoBuilder;
import iot.zjt.jclient.util.UriGenerator;

/**
 * @version 2018.10.28
 * @author Mr Dk.
 * The core connector of JClient
 */

public class JClientConnector {

    private final String host;
    private final int port;

    private boolean running;

    private List<JClientListener> allListeners;
    private Set<Class<? extends KismetInfo>> allSubscriptions;

    /**
     * To register a listener on this connection
     * Updated the subscriptions of this connection
     * @param listener
     */
    public void register(JClientListener listener) {
        if (!allListeners.contains(listener)) {
            allListeners.add(listener);
            updateSubscriptions();
        }
    }

    /**
     * To unregister a listener on this connection
     * Updated the subscriptions of this connection
     * @param listener
     */
    public void unregister(JClientListener listener) {
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
     * Consturctor
     * Generate a working thread
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
                long timestamp = 0;
                String res = null;

                while (running) {
        
                    res = HttpRequestBuilder.doGet(
                        UriGenerator.buildUri(host, port, TimestampInfo.class)
                    );
                    timestamp = 
                        ((TimestampInfo) publishInfo(JSON.parseObject(res), TimestampInfo.class))
                        .getSec();





                    try {
                        sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                
                publishTerminate();
        	}
        	
        }.start();

    }

    /**
     * Publish information to all subscribed listeners
     * @param jsonObj
     * @param clazz
     */
    private KismetInfo publishInfo(JSONObject obj, Class<? extends KismetInfo> clazz) {
        for (JClientListener listener : allListeners) {
            if (listener.getSubscription().contains(clazz)) {
                listener.OnInformation(
                    InfoBuilder.buildInfo(obj, clazz));
            }
        }
        return InfoBuilder.buildInfo(obj, clazz);
    }

    /**
     * Publish information from an info array
     * @param arr
     * @param clazz
     */
    private void publishInfo(JSONArray arr, Class<? extends KismetInfo> clazz) {
        for (int i = 0;i < arr.size(); i++) {
            publishInfo(arr.getJSONObject(i), clazz);
        }
    }

    /**
     * Pushlish terminate info to all listeners
     */
    private void publishTerminate() {
        for (JClientListener listener : allListeners) {
            listener.OnTerminate("Connector killed");
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
