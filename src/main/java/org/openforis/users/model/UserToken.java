package org.openforis.users.model;

import org.openforis.users.jooq.tables.pojos.OfUserToken;

public class UserToken extends OfUserToken {

	private static final long serialVersionUID = 1L;

	private User user;

	public UserToken() {
		super();
	}

	public UserToken(OfUserToken ofUserToken) {
		super(ofUserToken);
	}

	public UserToken(OfUserToken ofUserToken, User user) {
		super(ofUserToken);
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
