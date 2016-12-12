package org.openforis.users.web;

import java.util.HashMap;
import java.util.Map;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateViewRoute;

public class Views {

	public static Route home = (Request req, Response rsp) -> {
		return "Hello " + req.params(":name");
	};

	public static TemplateViewRoute home2 = (Request req, Response rsp) -> {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("name", req.params(":name"));
		return new ModelAndView(model, "home.ftl");
	};

}
