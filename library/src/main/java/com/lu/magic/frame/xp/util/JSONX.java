package com.lu.magic.frame.xp.util;


import androidx.core.util.Predicate;
import androidx.core.util.Supplier;

import com.lu.magic.frame.xp.bean.ContractRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Lu
 * @date 2023/9/21 15:02
 * @description org.JSON封装
 */
public class JSONX {
    private static final String TAG = "JSONX";

    public static Map<String, Object> toMap(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        Iterator<String> it = jsonObject.keys();
        Map<String, Object> map = new LinkedHashMap<>();
        while (it.hasNext()) {
            String k = it.next();
            map.put(k, jsonObject.opt(k));
        }
        return map;
    }

    public static double optDouble(JSONObject jsonObject, String key) {
        return optDouble(jsonObject, key, Double.NaN);
    }

    public static double optDouble(JSONObject jsonObject, String key, double fallback) {
        if (jsonObject == null) {
            return fallback;
        }
        return jsonObject.optDouble(key, fallback);
    }

    public static long optLong(JSONObject jsonObject, String key) {
        return optLong(jsonObject, key, 0L);
    }

    /**
     * 获取long类型数据（修正原org.json 库的bug，string 的数值转long存在精度缺失）
     *
     * @param jsonObject
     * @param key
     * @param fallback
     * @return
     */
    public static long optLong(JSONObject jsonObject, String key, long fallback) {
        if (jsonObject == null) {
            return fallback;
        }
        //return jsonObject.optLong(key, fallback);
        Object object = opt(jsonObject, key);
        Long result = null;
        try {
            result = toLong(object);
        } catch (Exception e) {

        }
        return result != null ? result : fallback;
    }

    static Long toLong(Object value) {
        if (value instanceof Long) {
            return (Long) value;
        } else if (value instanceof Number) {
            return ((Number) value).longValue();
        } else if (value instanceof String) {
            try {
                return Long.parseLong((String) value);
            } catch (NumberFormatException ignored) {
            }
        }
        return null;
    }

    public static int optInt(JSONObject source, String key) {
        return optInt(source, key, 0);
    }

    public static int optInt(JSONObject jsonObject, String key, int fallback) {
        if (jsonObject == null) {
            return fallback;
        }
        return jsonObject.optInt(key, fallback);
    }

    public static boolean optBoolean(JSONObject source, String key) {
        return optBoolean(source, key, false);
    }

    public static boolean optBoolean(JSONObject source, String key, boolean fallback) {
        if (source == null) {
            return fallback;
        }
        return source.optBoolean(key, fallback);
    }

    public static String optString(JSONObject jsonObject, String key) {
        return optString(jsonObject, key, null);
    }

    public static String optString(JSONObject jsonObject, String key, String fallback) {
        if (jsonObject == null) {
            return fallback;
        }
        if (jsonObject.isNull(key)) {
            return fallback;
        }
        Object da = jsonObject.opt(key);
        if (da == null) {
            return fallback;
        }
        return jsonObject.optString(key, fallback);
    }

    public static void put(JSONObject jsonObject, String key, boolean value) {
        if (jsonObject == null) {
            return;
        }
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void put(JSONObject jsonObject, String key, double value) {
        if (jsonObject == null) {
            return;
        }
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void put(JSONObject jsonObject, String key, long value) {
        if (jsonObject == null) {
            return;
        }
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void put(JSONObject jsonObject, String key, String value) {
        if (jsonObject == null) {
            return;
        }
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void put(JSONObject jsonObject, String key, int value) {
        if (jsonObject == null) {
            return;
        }
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void put(JSONObject jsonObject, String key, Object value) {
        if (jsonObject == null) {
            return;
        }
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void putOpt(JSONObject jsonObject, String key, Object value) {
        if (jsonObject == null) {
            return;
        }
        try {
            if (value instanceof Map) {
                try {
                    value = new JSONObject((Map) value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (value instanceof Collection) {
                try {
                    value = new JSONArray((Collection) value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            jsonObject.putOpt(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static Object opt(JSONObject source, String text) {
        if (source == null) {
            return null;
        }
        return source.opt(text);
    }

    public static JSONObject optJSONObject(JSONObject json, String account) {
        if (json == null) {
            return null;
        }
        return json.optJSONObject(account);
    }

    public static boolean isEmpty(JSONArray jsonArray) {
        if (jsonArray == null || jsonArray.length() == 0) {
            return true;
        }
        return false;
    }

    public static JSONObject optJSONObject(JSONArray jsonArray, int i) {
        if (jsonArray == null) {
            return null;
        }
        try {
            return jsonArray.optJSONObject(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Object> toList(JSONArray jsonArray) {
        if (jsonArray == null) {
            return null;
        }
        ArrayList<Object> list = new ArrayList<>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(jsonArray.opt(i));
        }
        return list;
    }


    public static Object filter(JSONArray jsonArray, boolean reverse, Predicate<Object> filter) {
        if (JSONX.isEmpty(jsonArray)) {
            return null;
        }
        int len = jsonArray.length();
        for (int i = 0; i < len; i++) {
            int index = i;
            if (reverse) {
                index = len - i - 1;
            }
            Object json = jsonArray.opt(index);
            if (filter.test(json)) {
                return json;
            }
        }
        return null;
    }

    public static <T> T filter(JSONObject jsonObject, boolean reverse, Class<T> tClass, Predicate<Object> filter) {
        Object result = filter(jsonObject.names(), reverse, filter);
        if (tClass != null && tClass.isInstance(result)) {
            return tClass.cast(result);
        }
        return null;
    }

    public static Object filter(JSONObject jsonObject, boolean reverse, Predicate<Object> filter) {
        if (JSONX.isEmpty(jsonObject)) {
            return null;
        }
        if (reverse) {
            JSONX.filter(jsonObject.names(), true, o -> {
                Object value = jsonObject.opt(o + "");
                return filter.test(value);
            });
        } else {
            Iterator<String> it = jsonObject.keys();
            while (it.hasNext()) {
                String key = it.next();
                if (filter.test(jsonObject.opt(key))) {
                    return jsonObject.opt(key);
                }
            }
        }
        return null;
    }


    private static boolean isEmpty(JSONObject jsonObject) {
        if (jsonObject == null || jsonObject.length() == 0) {
            return true;
        }
        return false;
    }

    public static Object remove(JSONObject mSourceData, String name) {
        if (mSourceData != null) {
            return mSourceData.remove(name);
        }
        return null;
    }

    /**
     * 获取JSONArray
     *
     * @param mSource
     * @param data
     * @return
     */
    public static JSONArray optJSONArray(JSONObject mSource, String data) {
        if (mSource == null) {
            return null;
        }
        return mSource.optJSONArray(data);
    }

    public static Set<String> optStringSet(JSONObject jsonObject, Set<String> fallback) {
        JSONArray arr = optJSONArray(jsonObject, "data");
        if (arr == null) {
            return null;
        }
        Set<String> result = new LinkedHashSet<>();
        for (int i = 0; i < arr.length(); i++) {
            result.add(arr.optString(i));
        }
        return result;
    }

    public static List<String> optStringList(JSONObject jsonObject) {
        JSONArray arr = optJSONArray(jsonObject, "data");
        if (arr == null) {
            return null;
        }
        List<String> result = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++) {
            result.add(arr.optString(i));
        }
        return result;
    }

    public static <T extends JsonObjectInterface> JSONArray toJsonArray(List<T> list) {
        if (list == null) {
            return null;
        }
        JSONArray result = new JSONArray();
        for (T action : list) {
            result.put(action.toJsonObject());
        }
        return result;
    }
    public static <T extends JsonObjectInterface> List<T> toList(JSONArray jsonArray, Supplier<T> beanCreateFactory) {
        if (jsonArray == null) {
            return null;
        }
        if (beanCreateFactory == null) {
            return null;
        }
        List<T> result = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            T t = beanCreateFactory.get();
            t.fromJsonObject(jsonObject);
            result.add(t);
        }
        return result;
    }

    public static <T extends JsonObjectInterface> List<T> toList(JSONArray jsonArray, Class<T> tClass) {
        return toList(jsonArray, () -> {
            try {
                return tClass.newInstance();
            } catch (IllegalAccessException | InstantiationException e) {
                 e.printStackTrace();
            }
            return null;
        });
    }

    public static JSONObject toJsonObject(String json) {
        try {
            return new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public interface JsonObjectInterface {
        JSONObject toJsonObject();
        JsonObjectInterface fromJsonObject(JSONObject jsonObject);
    }
}
