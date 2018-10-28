package iot.zjt.jclient.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import iot.zjt.jclient.JClientListener;
import iot.zjt.jclient.annotation.FieldAliase;
import iot.zjt.jclient.information.KismetInfo;
import iot.zjt.jclient.information.MessageInfo;
import iot.zjt.jclient.information.TimestampInfo;

/**
 * Generate different types of information form JSON object
 * @version 2018.10.28
 * @author Mr Dk.
 */

public class InfoBuilder {

    /**
     * Build a TimestampInfo object from Kismet server
     * @param origin
     * @param allListeners
     * @return The timestamp of Kismet server
     */
    public static final long publishTimestampInfo(
        String origin, List<JClientListener> allListeners) {

        JSONObject timestampObj = JSON.parseObject(origin);
        KismetInfo info = publishInfo(timestampObj, TimestampInfo.class, allListeners);
        return ((TimestampInfo) info).getSec();
    }

    /**
     * Build a MessageInfo object from Kismet server
     * @param origin
     * @param allListeners
     * @param timestamp
     */
    public static final void publishMessageInfo(
        String origin, List<JClientListener> allListeners, long timestamp) {

        JSONObject msgList = JSON.parseObject(origin);
        JSONArray msgArray = msgList.getJSONArray("kismet.messagebus.list");
        publishInfo(msgArray, MessageInfo.class, allListeners);
    }

    /**
     * Build a terminating message to all listeners
     * @param allListeners
     */
    public static final void buildTerminateInfo(List<JClientListener> allListeners) {
        synchronized(allListeners) {
            for (JClientListener listener : allListeners) {
                listener.OnTerminate("Connector killed");
            }
        }
    }

    /**
     * Build a KismetInfo object from JSONObject and specific Class type
     * @param json
     * @param clazz
     * @return A KismetInfo object
     */
    private static final KismetInfo buildInfo(
        JSONObject json, Class<? extends KismetInfo> clazz) {

        KismetInfo info = null;
        try {
            info = (KismetInfo) clazz.newInstance();
            for (Method method : clazz.getMethods()) {
                FieldAliase aliase = method.getAnnotation(FieldAliase.class);
                if (aliase != null &&
                    json.containsKey(aliase.value()) &&
                    method.getParameterTypes().length == 1) {

                    method.invoke(info, json.get(aliase.value()));
                }
            }

        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        
        return info;
    }

    /**
     * Publish the built infomation to all subscribed listeners
     * @param obj
     * @param clazz
     * @param allListeners
     * @return The built KismetInfo object
     */
    private static final KismetInfo publishInfo(
        JSONObject obj,
        Class<? extends KismetInfo> clazz, 
        List<JClientListener> allListeners)
    {
        synchronized(allListeners) {
            for (JClientListener listener : allListeners) {
                if (listener.getSubscription().contains(clazz)) {
                    listener.OnInformation(buildInfo(obj, clazz));
                }
            }
        }
        return buildInfo(obj, clazz);
    }

    /**
     * Publish informations built from JSONArray
     * @param arr
     * @param clazz
     * @param allListeners
     */
    private static final void publishInfo(
        JSONArray arr,
        Class<? extends KismetInfo> clazz,
        List<JClientListener> allListeners)
    {
        for (int i = 0; i < arr.size(); i++) {
            publishInfo(arr.getJSONObject(i), clazz, allListeners);
        }
    }
}
