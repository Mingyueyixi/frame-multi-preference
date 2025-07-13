package com.lu.magic.frame.xp.bean;

import android.text.TextUtils;

import androidx.annotation.Keep;

import com.lu.magic.frame.xp.annotation.FunctionValue;
import com.lu.magic.frame.xp.annotation.GroupValue;
import com.lu.magic.frame.xp.annotation.PreferenceIdValue;
import com.lu.magic.frame.xp.util.Ids;
import com.lu.magic.frame.xp.util.JSONX;

import org.json.JSONObject;

import java.util.List;

/**
 * ContentProvider约定的数据结构
 */
@Keep
public class ContractRequest implements JSONX.JsonObjectInterface {
    @PreferenceIdValue
    public String preferenceId;
    public String mode;
    public String table;
    @GroupValue
    public String group;
    public List<Action<?>> actions;

    public String requestId;

    public ContractRequest() {
    }

    static String genRequestId() {
        return Ids.Companion.genRequestId();
    }

    public ContractRequest(String preferenceId, String mode, String table, String group, List<Action<?>> actions) {
        this.preferenceId = preferenceId;
        this.mode = mode;
        this.table = table;
        this.group = group;
        this.actions = actions;
        this.requestId = genRequestId();
    }

    @Keep
    public static class Action<T> implements JSONX.JsonObjectInterface {
        @FunctionValue
        public String function;
        public String key;
        public T value;

        public Action() {
        }

        public Action(@FunctionValue String function, String key, T value) {
            this.function = function;
            this.key = key;
            this.value = value;
        }

        @Override
        public JSONObject toJsonObject() {
            JSONObject jsonObject = new JSONObject();
            JSONX.putOpt(jsonObject, "function", function);
            JSONX.putOpt(jsonObject, "key", key);
            JSONX.putOpt(jsonObject, "value", value);
            return jsonObject;
        }

        @Override
        public Action<T> fromJsonObject(JSONObject jsonObject) {
            function = JSONX.optString(jsonObject, "function");
            key = JSONX.optString(jsonObject, "key");
            value = (T) JSONX.opt(jsonObject, "value");
            return this;
        }

    }

    public JSONObject toJsonObject() {
        JSONObject jsonObject = new JSONObject();
        JSONX.putOpt(jsonObject, "preferenceId", preferenceId);
        JSONX.putOpt(jsonObject, "mode", mode);
        JSONX.putOpt(jsonObject, "table", table);
        JSONX.putOpt(jsonObject, "group", group);
        JSONX.putOpt(jsonObject, "actions", JSONX.toJsonArray(actions));
        JSONX.putOpt(jsonObject, "requestId", requestId);
        return jsonObject;
    }

    @Override
    public ContractRequest fromJsonObject(JSONObject jsonObject) {
        this.preferenceId = JSONX.optString(jsonObject, "preferenceId");
        this.mode = JSONX.optString(jsonObject, "mode");
        this.table = JSONX.optString(jsonObject, "table");
        this.group = JSONX.optString(jsonObject, "group");
        this.actions = JSONX.toList(JSONX.optJSONArray(jsonObject, "actions"), Action::new);
        this.requestId = JSONX.optString(jsonObject, "requestId");
        return this;
    }

    public String toJson() {
        return toJsonObject().toString();
    }

    public static ContractRequest fromJson(String json) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        return new ContractRequest().fromJsonObject(JSONX.toJsonObject(json));
    }
}
