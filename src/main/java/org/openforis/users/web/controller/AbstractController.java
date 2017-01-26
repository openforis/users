package org.openforis.users.web.controller;

import spark.Request;

public abstract class AbstractController {

	protected long getLongParam(Request req, String param) {
		String idParam = req.params(param);
		return Long.parseLong(idParam);
	}

}
