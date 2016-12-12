package org.openforis.users.web;

import static spark.Spark.get;

import org.openforis.users.manager.EntityManagerFactory;

import spark.Request;
import spark.Response;
import spark.Route;
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
		get("/users/all", "application/json", listAllUsers(), new JsonTransformer());
	}

	private Route listAllUsers() {
		return new Route() {
			public Object handle(Request request, Response response) throws Exception {
				return EntityManagerFactory.getUserManager().listAll();
			}
		};
	}

}
