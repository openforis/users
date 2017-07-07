package org.openforis.users.web.controller;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
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

	protected String getStringParam(Request req, String param) {
		return getStringParam(req, param, null);
	}

	protected String getStringParam(Request req, String param, String defaultValue) {
		String value = getParamOrQueryParam(req, param);
		return value == null ? defaultValue : value;
	}

	protected Long getLongParam(Request req, String param) {
		String strParam = getParamOrQueryParam(req, param);
		return Long.parseLong(strParam);
	}

	protected BigDecimal getBigDecimalFromBody(Request req, String param) {
		String strParam = getParamOrQueryParam(req, param);
		return strParam == null ? null : new BigDecimal(strParam);
	}

	protected Boolean getBooleanParam(Request req, String param) {
		return getBooleanParam(req, param, null);
	}

	protected Boolean getBooleanParam(Request req, String param, Boolean defaultValue) {
		String strParam = getParamOrQueryParam(req, param);
		return strParam == null ? defaultValue : Boolean.valueOf(strParam);
	}

	protected <T extends Enum<T>> T getEnumParam(Request req, String param, Class<T> enumType) {
		return getEnumParam(req, param, enumType, null);
	}

	protected <T extends Enum<T>> T getEnumParam(Request req, String param, Class<T> enumType, T defaultValue) {
		String enumName = getStringParam(req, param);
		return enumName == null ? defaultValue : Enum.valueOf(enumType, enumName);
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

	protected String getParamOrQueryParam(Request req, String param) {
		String result = req.params(param);
		if (result == null) {
			result = req.queryParams(param);
		}
		return result;
	}

}
