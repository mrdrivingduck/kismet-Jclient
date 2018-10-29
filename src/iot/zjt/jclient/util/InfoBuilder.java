package iot.zjt.jclient.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.alibaba.fastjson.JSONObject;

import iot.zjt.jclient.annotation.FieldAliase;
import iot.zjt.jclient.information.KismetInfo;

/**
 * Generate different types of information form JSON object
 * @version 2018.10.29
 * @author Mr Dk.
 */

public class InfoBuilder {

    /**
     * Build a KismetInfo object from JSONObject and specific Class type
     * @param json
     * @param clazz
     * @return A KismetInfo object
     */
    public static final KismetInfo buildInfo(
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

}
