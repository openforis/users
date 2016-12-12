package org.openforis.users.manager;

public class EntityManagerFactory {

	private static UserManager userManager;

	public static UserManager getUserManager() {
		if (userManager == null) {
			userManager = new UserManager();
		}
		return userManager;
	}
	
}
