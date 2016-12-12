package org.openforis.users.web;

import spark.Request;
import spark.Response;
import spark.Route;

public class Views {

	public static Route home = (Request req, Response rsp) -> {
		return "Hello1 " + req.params(":name");
	};

}
