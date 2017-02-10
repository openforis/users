package org.openforis.users.auth;

import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.core.config.ConfigFactory;
import org.pac4j.core.context.HttpConstants.HTTP_METHOD;
import org.pac4j.core.matching.HttpMethodMatcher;
import org.pac4j.http.client.direct.DirectBasicAuthClient;
import org.pac4j.http.client.direct.IpClient;
import org.pac4j.http.client.indirect.IndirectBasicAuthClient;
import org.pac4j.http.credentials.authenticator.IpRegexpAuthenticator;

public class UsersConfigFactory implements ConfigFactory {

	public Config build() {

		final DirectBasicAuthClient directBasicAuthClient = new DirectBasicAuthClient();
		directBasicAuthClient.setAuthenticator(new UsersAuthenticator());
		directBasicAuthClient.setProfileCreator(new UsersProfileCreator());

		final IndirectBasicAuthClient indirectBasicAuthClient = new IndirectBasicAuthClient();
		indirectBasicAuthClient.setAuthenticator(new UsersAuthenticator());
		indirectBasicAuthClient.setProfileCreator(new UsersProfileCreator());

		final IpClient ipClient = new IpClient(new IpRegexpAuthenticator("127\\..*"));

		Clients clients = new Clients(directBasicAuthClient, indirectBasicAuthClient, ipClient);

		Config config = new Config(clients);
		config.setMatcher(new HttpMethodMatcher(HTTP_METHOD.GET, HTTP_METHOD.POST, HTTP_METHOD.PUT, HTTP_METHOD.DELETE));
		config.setHttpActionAdapter(new UsersHttpActionAdapter());
		return config;
	}

}
