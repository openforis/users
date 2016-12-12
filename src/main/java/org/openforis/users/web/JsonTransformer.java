package org.openforis.users.web;

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
        return gson.fromJson(json, Map.class);
    }

}
