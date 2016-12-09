package org.openforis.users;

import static spark.Spark.get;

import spark.servlet.SparkApplication;

/**
 * 
 * @author R. Fontanarosa
 * @author S. Ricci
 *
 */
public class SparkInit implements SparkApplication {

	@Override
	public void init() {
		get("/hello/:name", (request, response) -> "Hello " + request.params(":name"));
	}

}
