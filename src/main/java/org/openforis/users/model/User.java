package org.openforis.users.model;

import org.openforis.users.jooq.tables.pojos.OfUser;

/**
 * 
 * @author S. Ricci
 *
 */
public class User extends org.openforis.users.jooq.tables.pojos.OfUser implements IdentifiableObject {

	private static final long serialVersionUID = 1L;
	
	public User() {
	}
	
	public User(OfUser value) {
		super(value);
	}

	private String rawPassword;

	public String getRawPassword() {
		return rawPassword;
	}

	public void setRawPassword(String rawPassword) {
		this.rawPassword = rawPassword;
	}
	
}
