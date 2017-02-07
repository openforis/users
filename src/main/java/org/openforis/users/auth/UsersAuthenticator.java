package org.openforis.users.auth;

import org.openforis.users.manager.EntityManagerFactory;
import org.openforis.users.manager.UserManager;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.UsernamePasswordCredentials;
import org.pac4j.core.credentials.authenticator.Authenticator;
import org.pac4j.core.exception.CredentialsException;
import org.pac4j.core.exception.HttpAction;
import org.pac4j.core.util.CommonHelper;

public class UsersAuthenticator implements Authenticator<UsernamePasswordCredentials> {

	public UserManager USER_MANAGER = EntityManagerFactory.getInstance().getUserManager();

	@Override
	public void validate(final UsernamePasswordCredentials credentials, final WebContext context) throws HttpAction {
		if (credentials == null) {
			throwsException("No credential");
		}
		String username = credentials.getUsername();
		String password = credentials.getPassword();
		if (CommonHelper.isBlank(username)) {
			throwsException("Username cannot be blank");
		}
		if (CommonHelper.isBlank(password)) {
			throwsException("Password cannot be blank");
		}
		if (!USER_MANAGER.verifyPassword(username, password)) {
			throwsException("Username : '" + username + "' does not match password");
		}
	}

	protected void throwsException(final String message) {
		throw new CredentialsException(message);
	}

}
