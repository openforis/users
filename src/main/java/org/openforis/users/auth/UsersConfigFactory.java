package org.openforis.users.auth;

import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.core.config.ConfigFactory;
import org.pac4j.http.client.direct.DirectBasicAuthClient;

public class UsersConfigFactory implements ConfigFactory {

	public Config build() {
		final DirectBasicAuthClient directBasicAuthClient = new DirectBasicAuthClient();
		directBasicAuthClient.setAuthenticator(new UsersAuthenticator());
		directBasicAuthClient.setProfileCreator(new UsersProfileCreator());
		//Clients clients = new Clients(directBasicAuthClient);
		//Config config = new Config(clients);
		Config config = new Config(directBasicAuthClient);
		config.setHttpActionAdapter(new UsersHttpActionAdapter());
		return config;
	}

}
