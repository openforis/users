package org.openforis.users.web.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.BeanUtils;
import org.openforis.users.web.JsonTransformer;

import spark.Request;

public abstract class AbstractController {

	protected JsonTransformer jsonTransformer;
	
	public AbstractController(JsonTransformer jsonTransformer) {
		super();
		this.jsonTransformer = jsonTransformer;
	}

	protected long getLongParam(Request req, String param) {
		String idParam = req.params(param);
		return Long.parseLong(idParam);
	}

	protected void setNotNullParams(Request req, Object to) throws IllegalAccessException, InvocationTargetException {
		Map<String, Object> bodyMap = jsonTransformer.parse(req.body());
		for (Entry<String, Object> entry : bodyMap.entrySet()) {
			Object value = entry.getValue();
			if (value != null) {
				BeanUtils.setProperty(to, entry.getKey(), value);
			}
		}
	}

}
