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

	private String plainPassword;

	public String getPlainPassword() {
		return plainPassword;
	}

	public void setPlainPassword(String plainPassword) {
		this.plainPassword = plainPassword;
	}
	
}
