package org.openforis.users;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.openforis.users.manager.EntityManagerFactory;
import org.openforis.users.manager.UserManager;
import org.openforis.users.model.User;

public class UserManagerTest extends AbstractTest {

	@Test
	public void listAll() {
		UserManager userManager = EntityManagerFactory.getInstance().getUserManager();
		
		// create the objects needed for testing
		User user = new User();
		user.setUsername("test_user");
		user.setPassword("test_pass");
		// storing the objects for the test in the database
		
		userManager.save(user);
		
		List<User> users = userManager.findAll();
		
		assertNotNull(users);
		assertEquals(1, users.size());
	}
}
