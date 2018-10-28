package iot.zjt.jclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import iot.zjt.jclient.information.KismetInfo;
import iot.zjt.jclient.information.MessageInfo;
import iot.zjt.jclient.information.TimestampInfo;
import iot.zjt.jclient.util.HttpRequestBuilder;
import iot.zjt.jclient.util.InfoBuilder;
import iot.zjt.jclient.util.UriGenerator;

/**
 * The core connector of JClient
 * @version 2018.10.28
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
        
                    timestamp = generateTimestampInfo();

                    try {
                        sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                
                InfoBuilder.buildTerminateInfo(allListeners);

                try {
                    HttpRequestBuilder.getHttpClient().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        	}
        }.start();
    }

    /**
     * To generate a MessageInfo
     * @param timestamp
     */
    private void generateMessageInfo(long timestamp) {
        String res = HttpRequestBuilder.doGet(
            UriGenerator.buildUri(host, port, MessageInfo.class, timestamp)
        );
        InfoBuilder.publishMessageInfo(res, allListeners, timestamp);
    }

    /**
     * To generate a TimestampInfo
     * @return The timestamp of Kismet server
     */
    private long generateTimestampInfo() {
        String res = HttpRequestBuilder.doGet(
            UriGenerator.buildUri(host, port, TimestampInfo.class)
        );
        return InfoBuilder.publishTimestampInfo(res, allListeners);
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
