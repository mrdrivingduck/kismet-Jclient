package iot.zjt.jclient.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;

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
            Method[] methods = clazz.getMethods();
            Arrays.sort(methods, new Comparator<Method>() {
                @Override
                public int compare(Method m1, Method m2) {
                    return m1.getName().compareTo(m2.getName());
                }
            });
            for (Method method : methods) {
                FieldAliase aliase = method.getAnnotation(FieldAliase.class);
                if (aliase != null &&
                    json.containsKey(aliase.value()) &&
                    method.getParameterTypes().length == 1) {
                    // System.out.println(method.getName());
                    // System.out.println(method.getParameterTypes()[0].getName());
                    method.invoke(msg, json.get(aliase.value()));
                }
            }

        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        
        return msg;
    }

}
