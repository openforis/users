package org.openforis.users.manager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Hex;
import org.openforis.users.dao.UserDao;
import org.openforis.users.dao.UserGroupDao;
import org.openforis.users.jooq.tables.pojos.OfUser;
import org.openforis.users.model.Group;
import org.openforis.users.model.Group.Visibility;
import org.openforis.users.model.User;
import org.openforis.users.model.UserGroup;
import org.openforis.users.model.UserGroup.UserGroupRequestStatus;
import org.openforis.users.model.UserGroup.UserGroupRole;

/**
 * 
 * @author S. Ricci
 *
 */
public class UserManager extends AbstractManager<User, UserDao> {

	private static final String PASSWORD_PATTERN = "^.{6,100}$";

	private GroupManager groupManager;
	private ResourceGroupManager resourceGroupManager;
	private UserGroupDao userGroupDao;
	
	public UserManager(UserDao userDao, UserGroupDao userGroupDao, 
			GroupManager groupManager, ResourceGroupManager resourceGroupManager) {
		super(userDao, User.class);
		this.userGroupDao = userGroupDao;
		this.groupManager = groupManager;
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
	protected void update(User user) {
		try {
			String plainPassword = user.getRawPassword();
			if (plainPassword != null) {
				String encodedPassword = checkAndEncodePassword(plainPassword);
				user.setPassword(encodedPassword);
			} else {
				OfUser oldUser = dao.findById(user.getId());
				user.setPassword(oldUser.getPassword());
			}
			super.update(user);
		} catch (Exception e) {
		}
	}

	@Override
	protected void insert(User user) {
		try {
			String plainPassword = user.getRawPassword();
			String encodedPassword = checkAndEncodePassword(plainPassword);
			user.setPassword(encodedPassword);
			super.insert(user);
			createAndInsertPrivateGroup(user);
		} catch (Exception e) {
		}
	}

	public boolean verifyPassword(String username, String rawPassword) {
		User user = findByUsername(username);
		if (user != null) {
			String encodedPassword = encodePassword(rawPassword);
			return user.getPassword().equals(encodedPassword);
		} else {
			return false;
		}
	}

	public void changePassword(String username, String newPassword) {
		User user = findByUsername(username);
		if (user != null) {
			user.setRawPassword(newPassword);
			save(user);
		} else {
			throw new IllegalArgumentException(String.format("User %s not found." + username));
		}
	}

	public boolean isEnabled(String username) {
		User user = findByUsername(username);
		if (user != null) {
			return user.getEnabled();
		} else {
			throw new IllegalArgumentException(String.format("User %s not found." + username));
		}
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
		
		//add user-privateGroup relation
		UserGroup userGroup = new UserGroup();
		userGroup.setUserId(user.getId());
		userGroup.setGroupId(privateGroup.getId());
		userGroup.setStatusCode(UserGroupRequestStatus.ACCEPTED.getCode());
		userGroup.setRoleCode(UserGroupRole.OWNER.getCode());
		userGroupDao.insert(userGroup);
	}

	private String checkAndEncodePassword(String plainPassword) throws Exception {
		boolean matchesPattern = Pattern.matches(PASSWORD_PATTERN, plainPassword);
		if (matchesPattern) {
			return encodePassword(plainPassword);
		} else {
			throw new Exception("", new Throwable(""));
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

}
