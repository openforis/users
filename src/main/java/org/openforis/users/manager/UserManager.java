package org.openforis.users.manager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Hex;
import org.openforis.users.dao.UserDao;
import org.openforis.users.jooq.tables.pojos.OfUser;
import org.openforis.users.model.Group;
import org.openforis.users.model.Group.Visibility;
import org.openforis.users.model.User;
import org.openforis.users.model.UserGroup.UserGroupRole;

/**
 * 
 * @author S. Ricci
 *
 */
public class UserManager extends AbstractManager<User, UserDao> {

	private static final String PASSWORD_PATTERN = "^\\w{5,}$"; // alphanumeric, at least 5 letters

	private GroupManager groupManager;
	private UserGroupManager userGroupManager;
	private ResourceGroupManager resourceGroupManager;
	
	public UserManager(UserDao userDao, GroupManager groupManager, 
			UserGroupManager userGroupManager, ResourceGroupManager resourceGroupManager) {
		super(userDao, User.class);
		this.groupManager = groupManager;
		this.userGroupManager = userGroupManager;
		this.resourceGroupManager = resourceGroupManager;
	}

	@Override
	public List<User> findAll() {
		return super.findAll();
	}
	
	@Override
	public User findById(Long id) {
		return super.findById(id);
	}
	
	public User findByUsername(String username) {
		return dao.findByUsername(username);
	}

	@Override
	public void save(User user) {
		String rawPassword = user.getRawPassword();
		String encodedPassword = checkAndEncodePassword(rawPassword);
		user.setPassword(encodedPassword);
		super.save(user);
	}

	@Override
	protected void update(User user) {
		String plainPassword = user.getRawPassword();
		if (plainPassword != null) {
			String encodedPassword = checkAndEncodePassword(plainPassword);
			user.setPassword(encodedPassword);
		} else {
			OfUser oldUser = dao.findById(user.getId());
			user.setPassword(oldUser.getPassword());
		}
		super.update(user);
	}

	@Override
	protected void insert(User user) {
		String plainPassword = user.getRawPassword();
		String encodedPassword = checkAndEncodePassword(plainPassword);
		user.setPassword(encodedPassword);
		super.insert(user);
		createAndInsertPrivateGroup(user);
	}

	public boolean verifyPassword(String username, String plainPassword) {
		User user = findByUsername(username);
		if (user == null) {
			throw new IllegalArgumentException("User not found: " + username);
		}
		String encodedPassword = encodePassword(plainPassword);
		return user.getPassword().equals(encodedPassword);
	}
	
	@Override
	public void deleteById(final long id) {
		runInTransaction(new Runnable() {
			public void run() {
				//check that there aren't any resources associated to the user (to its groups)
				int resourceCount = resourceGroupManager.countResourcesByUserId(id);
				if (resourceCount == 0) {
					groupManager.deleteByUserId(id);
					dao.deleteById(id);
				} else {
					throw new IllegalStateException("Cannot delete user with id %d; there are resources associated to one of its user groups.");
				}
			}
		});
	}
	
	private void createAndInsertPrivateGroup(User user) {
		Group privateGroup = new Group();
		privateGroup.setName(user.getUsername() + "_private_group");
		privateGroup.setLabel(user.getUsername() + " Private Group");
		privateGroup.setVisibility(Visibility.PRIVATE);
		privateGroup.setSystemDefined(true);
		privateGroup.setEnabled(true);
		groupManager.insert(privateGroup);
		userGroupManager.join(privateGroup, user, UserGroupRole.OWNER);
	}
	
	private String checkAndEncodePassword(String plainPassword)  {
		boolean matchesPattern = Pattern.matches(PASSWORD_PATTERN, plainPassword);
		if (matchesPattern) {
			return encodePassword(plainPassword);
		} else {
			throw new InvalidUserPasswordException();
		}
	}
	
	private String encodePassword(String plainPassword) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			byte[] digest = messageDigest.digest(plainPassword.getBytes());
			char[] resultChar = Hex.encodeHex(digest);
			return new String(resultChar);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Error encoding user password", e);
		}
	}
	
	private static class InvalidUserPasswordException extends RuntimeException {

		private static final long serialVersionUID = 1L;
		
	}
}
