package iot.zjt.jclient;

import java.util.HashSet;
import java.util.Set;

import iot.zjt.jclient.message.KismetMessage;

/**
 * A listener which can subscribe and listen several type of KismetMessage
 * @version 2018.10.30
 * @author Mr Dk.
 */

public abstract class JClientListener {

    private Set<Class<? extends KismetMessage>> subscription;
    private JClientConnector conn;

    public JClientListener() {
        this.subscription = new HashSet<>();
        this.conn = null;
    }

    /**
     * Be able to call JClientConnector's function,
     * to update the subscriptions of JClientConnector
     * @param conn
     * @throws IllegalArgumentException
     */
    public void setConn(JClientConnector conn) throws IllegalArgumentException {
        if (this.conn != null) {
            throw new IllegalArgumentException("Listener has been registered on another Connector");
        }
        this.conn = conn;
    }

    public Set<Class<? extends KismetMessage>> getSubscription() {
        return subscription;
    }

    /**
     * Subscribe a KismetMessage type if not subscribing,
     * update the Connector's subscriptions if registed to a Connector
     * @param clazz
     */
    public void subscribe(Class<? extends KismetMessage> clazz) {
        if (!subscription.contains(clazz)) {
            subscription.add(clazz);
            if (conn != null) {
                conn.updateSubscriptions();
            }
        }
    }

    /**
     * Unsubscribe a KismetMessage type if subscribing,
     * update the Connector's subscriptions if registed to a Connector
     * @param clazz
     */
    public void unsubscribe(Class<? extends KismetMessage> clazz) {
        if (subscription.contains(clazz)) {
            subscription.remove(clazz);
            if (conn != null) {
                conn.updateSubscriptions();
            }
        }
    }

    /**
     * Called when a KismetMessage is built,
     * needed to be OVERRIDE
     * @param info
     */
    public abstract void onMessage(KismetMessage msg);

    /**
     * Called when working thread is killed
     * @param reason
     */
    public abstract void onTerminate(String reason);
}
