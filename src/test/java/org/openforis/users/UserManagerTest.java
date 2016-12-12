package org.openforis.users;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.junit.Test;
import org.openforis.users.model.User;

public class UserManagerTest extends AbstractTest {

	@Test
	public void listAll() {
		session.beginTransaction();
		// create the objects needed for testing
		User user = new User();
		user.setUsername("test_user");
		user.setPassword("test_pass");
		// storing the objects for the test in the database
		session.save(user);
		session.getTransaction().commit();
		
		CriteriaBuilder builder = session.getCriteriaBuilder();
		
		CriteriaQuery<User> criteria = builder.createQuery(User.class);
		criteria.select( criteria.from(User.class ) );

		List<User> users = session.createQuery( criteria ).getResultList();
		
		assertNotNull(users);
		assertEquals(1, users.size());
	}
}
