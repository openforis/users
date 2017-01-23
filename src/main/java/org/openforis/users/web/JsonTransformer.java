package org.openforis.users.web;

import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.Gson;

import spark.ResponseTransformer;

public class JsonTransformer implements ResponseTransformer {

	private Gson gson = new Gson();

    @Override
    public String render(Object model) {
        return gson.toJson(model);
    }

    @SuppressWarnings("unchecked")
	public Map<String, Object> parse(String json) {
        return parse(json, Map.class);
    }

    public <T> T parse(String json, Class<T> type) {
    	return gson.fromJson(json, type);
    }
    
    public <T> T parse(String json, Type type) {
    	return gson.fromJson(json, type);
    }
    
}
