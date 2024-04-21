package com.example.aria;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class JSONObjectToJsonObjectConverter {

    public static JsonObject convertToGsonJsonObject(JSONObject jsonObject) throws JSONException {
        JsonObject gsonJsonObject = new JsonObject();

        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Object value = jsonObject.get(key);
            if (value instanceof JSONObject) {
                // If the value is another JSONObject, recursively convert it
                gsonJsonObject.add(key, convertToGsonJsonObject((JSONObject) value));
            } else {
                gsonJsonObject.addProperty(key, value.toString());
            }
        }

        return gsonJsonObject;
    }
}
