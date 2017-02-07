package org.openforis.users.auth;

import org.pac4j.core.context.Pac4jConstants;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.UsernamePasswordCredentials;
import org.pac4j.core.exception.HttpAction;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.creator.ProfileCreator;

public class UsersProfileCreator implements ProfileCreator<UsernamePasswordCredentials, CommonProfile> {

	@Override
	public CommonProfile create(UsernamePasswordCredentials credentials, WebContext context) throws HttpAction {
		CommonProfile profile = new CommonProfile();
		profile.addAttribute(Pac4jConstants.USERNAME, credentials.getUsername());
		return profile;
	}

}
