package iot.zjt.jclient.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import iot.zjt.jclient.annotation.FieldAliase;
import iot.zjt.jclient.annotation.FieldPath;
import iot.zjt.jclient.information.KismetInfo;

public class JsonParamBuilder {

    private List<List<String>> allFields;
    private List<List<String>> allRegex;

    public JsonParamBuilder() {
        this.allFields = new ArrayList<>();
        this.allRegex = new ArrayList<>();
    }

    public void addFields(Class<? extends KismetInfo> clazz) {
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

    public void addRegex(String path, String regex) {
        List<String> pair = new ArrayList<>();
        pair.add(path);
        pair.add(regex);
        allRegex.add(pair);
    }

    public String build() {
        Map<String, Object> map = new HashMap<>();
        map.put("fields", allFields);
        map.put("regex", allRegex);
        return new JSONObject(map).toJSONString();
    }

}
