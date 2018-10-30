package iot.zjt.jclient.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.alibaba.fastjson.JSONObject;

import iot.zjt.jclient.annotation.FieldAliase;
import iot.zjt.jclient.message.KismetMessage;

/**
 * Generate different types of message form JSON object
 * @version 2018.10.30
 * @author Mr Dk.
 */

public class MsgBuilder {

    /**
     * Build a KismetMessage object from JSONObject and specific Class type
     * @param json
     * @param clazz
     * @return A KismetMessage object
     */
    public static final KismetMessage buildMessage(
        JSONObject json, Class<? extends KismetMessage> clazz) {

        KismetMessage msg = null;
        try {
            msg = (KismetMessage) clazz.newInstance();
            for (Method method : clazz.getMethods()) {
                FieldAliase aliase = method.getAnnotation(FieldAliase.class);
                if (aliase != null &&
                    json.containsKey(aliase.value()) &&
                    method.getParameterTypes().length == 1) {

                    method.invoke(msg, json.get(aliase.value()));
                }
            }

        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        
        return msg;
    }

}
