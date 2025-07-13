package com.lu.magic.frame.xp.bean;

import android.text.TextUtils;

import com.lu.magic.frame.xp.util.JSONX;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Set;

public class ContractResponse2 {
    private JSONObject mSource;

    public void setException(Throwable exception) {
        JSONX.putOpt(mSource, "exception", exception);
    }

    public Exception getException() {
        Object msg = JSONX.opt(mSource, "exception");
        if (msg instanceof String) {
            return new Exception((String) msg);
        }
        return null;
    }

    public String getResponseId() {
        return JSONX.optString(mSource, "responseId");
    }

    public <T> T getData() {
        return (T) JSONX.opt(mSource, "data");
    }

    public void setData(Object data) {
        JSONX.putOpt(mSource, "data", data);
    }

    public String getDataAsString(String fallback) {
        return JSONX.optString(mSource, "data", fallback);
    }

    public int getDataAsInt(int fallback) {
        return JSONX.optInt(mSource, "data", fallback);
    }

    public long getDataAsLong(long fallback) {
        return JSONX.optLong(mSource, "data", fallback);
    }

    public boolean getDataAsBoolean() {
        return JSONX.optBoolean(mSource, "data");
    }

    public boolean getDataAsBoolean(boolean fallback) {
        return JSONX.optBoolean(mSource, "data", fallback);
    }


    public double getDataAsDouble(double fallback) {
        return JSONX.optDouble(mSource, "data", fallback);
    }

    public float getDataAsFloat(float defValue) {
        return (float) JSONX.optDouble(mSource, "data", defValue);
    }

    public Set<String> getDataAsStringSet(Set<String> fallback) {
        return JSONX.optStringSet(mSource, fallback);
    }

    public Map<String, Object> getDataAsMapString() {
        return JSONX.toMap(JSONX.optJSONObject(mSource, "data"));
    }


    public ContractResponse2() {
        this(null, null);
    }

    public ContractResponse2(Object data, Throwable exception) {
        this.mSource = new JSONObject();
        this.setException(exception);
        this.setData(data);
    }

    public ContractResponse2(JSONObject jsonObject) {
        if (jsonObject == null) {
            jsonObject = new JSONObject();
        }
        this.mSource = jsonObject;
    }

    public static ContractResponse2 fromJson(String json) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        try {
            JSONObject jsonObject = new JSONObject(json);
            return new ContractResponse2(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String toJson() {
        return mSource.toString();
    }
}
