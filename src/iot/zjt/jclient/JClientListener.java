package iot.zjt.jclient;

import java.util.HashSet;
import java.util.Set;

import iot.zjt.jclient.information.KismetInfo;

/**
 * @version 2018.10.28
 * @author Mr Dk.
 * A listener which can subscribe and listen several type of KismetInfo
 */

public abstract class JClientListener {

    private Set<Class<? extends KismetInfo>> subscription;
    private JClientConnector conn;

    public JClientListener() {
        this.subscription = new HashSet<>();
        this.conn = null;
    }

    /**
     * Be able to call JClientConnector's function
     * To update the subscriptions of JClientConnector
     * @param conn
     * @throws IllegalArgumentException
     */
    public void setConn(JClientConnector conn) throws IllegalArgumentException {
        if (this.conn != null) {
            throw new IllegalArgumentException("Listener has been registered on another Connector");
        }
        this.conn = conn;
    }

    public Set<Class<? extends KismetInfo>> getSubscription() {
        return subscription;
    }

    /**
     * Subscribe a KismetInfo type if not subscribing
     * Update the Connector's subscriptions if registed to a Connector
     * @param clazz
     */
    public void subscribe(Class<? extends KismetInfo> clazz) {
        if (!subscription.contains(clazz)) {
            subscription.add(clazz);
            if (conn != null) {
                conn.updateSubscriptions();
            }
        }
    }

    /**
     * Unsubscribe a KismetInfo type if subscribing
     * Update the Connector's subscriptions if registed to a Connector
     * @param clazz
     */
    public void unsubscribe(Class<? extends KismetInfo> clazz) {
        if (subscription.contains(clazz)) {
            subscription.remove(clazz);
            if (conn != null) {
                conn.updateSubscriptions();
            }
        }
    }

    /**
     * Called when a KismetInfo is built
     * Need to be OVERRIDE
     * @param info
     */
    public abstract void OnInformation(KismetInfo info);
    public abstract void OnTerminate(String reason);
}
