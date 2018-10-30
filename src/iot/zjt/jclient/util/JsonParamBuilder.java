package iot.zjt.jclient.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import iot.zjt.jclient.annotation.FieldAliase;
import iot.zjt.jclient.annotation.FieldPath;
import iot.zjt.jclient.message.KismetMessage;

/**
 * Generate a JSON parameter to filter fields from Kismet Server
 * @version 2018.10.29
 * @author Mr Dk.
 */

public class JsonParamBuilder {

    private List<List<String>> allFields;
    private List<List<String>> allRegex;

    public JsonParamBuilder() {
        this.allFields = new ArrayList<>();
        this.allRegex = new ArrayList<>();
    }

    /**
     * Add fields that a message needs through annotation
     * @param clazz
     */
    public void addFields(Class<? extends KismetMessage> clazz) {
        for (Method method : clazz.getMethods()) {
            FieldPath path = method.getAnnotation(FieldPath.class);
            FieldAliase aliase = method.getAnnotation(FieldAliase.class);
            if (path != null && aliase != null) {
                List<String> pair = new ArrayList<>();
                pair.add(path.value());
                pair.add(aliase.value());
                allFields.add(pair);
            }
        }
    }

    /**
     * Add conditions to filter messages
     * @param path
     * @param regex
     */
    public void addRegex(String path, String regex) {
        List<String> pair = new ArrayList<>();
        pair.add(path);
        pair.add(regex);
        allRegex.add(pair);
    }

    /**
     * Build the JSON parameter string from lists
     * @return The Generated JSON string
     */
    public String build() {
        Map<String, Object> map = new HashMap<>();
        map.put("fields", allFields);
        map.put("regex", allRegex);
        return new JSONObject(map).toJSONString();
    }

}
