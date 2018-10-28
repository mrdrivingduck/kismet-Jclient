package iot.zjt.jclient;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSON;

import iot.zjt.jclient.information.KismetInfo;
import iot.zjt.jclient.util.HttpRequestGenerator;
import iot.zjt.jclient.util.InfoGenerator;
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

                while (running) {

                    for (Class<? extends KismetInfo> clazz : allSubscriptions) {
        
                        String json = HttpRequestGenerator.doGet(
                            UriGenerator.buildUri(
                                host, port, clazz
                            )
                        );
                        
                        for (JClientListener listener : allListeners) {
                            if (listener.getSubscription().contains(clazz)) {
                                listener.OnInformation(InfoGenerator.generateInfo(JSON.parseObject(json), clazz));
                            }
                        }
                    }

                    try {
                        sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    
                }
                
                for (JClientListener listeners : allListeners) {
                    listeners.OnTerminate("Connector killed");
                }
        	}
        	
        }.start();

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
