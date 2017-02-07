package org.openforis.users.auth;

import static spark.Spark.halt;

import org.pac4j.core.context.HttpConstants;
import org.pac4j.sparkjava.DefaultHttpActionAdapter;
import org.pac4j.sparkjava.SparkWebContext;

public class UsersHttpActionAdapter extends DefaultHttpActionAdapter {

	@Override
	public Object adapt(int code, SparkWebContext context) {
		if (code == HttpConstants.UNAUTHORIZED) {
			halt(401, "401");
		} else if (code == HttpConstants.FORBIDDEN) {
			halt(403, "403");
		} else {
			return super.adapt(code, context);
		}
		return null;
	}
}
