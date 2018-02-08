package org.openforis.users.manager;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.openforis.users.dao.UserTokenDao;
import org.openforis.users.jooq.tables.pojos.OfUserToken;
import org.openforis.users.model.User;
import org.openforis.users.model.UserToken;

public class UserTokenManager {

	private UserTokenDao userTokenDao;
	private UserManager userManager;

	public UserTokenManager(UserTokenDao userTokenDao, UserManager userManager) {
		super();
		this.userTokenDao = userTokenDao;
		this.userManager = userManager;
	}

	public List<UserToken> findByUser(Long id) {
		List<OfUserToken> ofUserTokens = userTokenDao.fetchByUserId(id);
		return fill(ofUserTokens);
	}

	public UserToken insert(long userId, String token) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		return insert(userId, token, timestamp);
	}

	public UserToken insert(long userId, String token, Timestamp tokenDatetime) {
		UserToken userToken = new UserToken();
		userToken.setUserId(userId);
		userToken.setToken(token);
		userToken.setTokenDatetime(tokenDatetime);
		userTokenDao.insert(userToken);
		return userToken;
	}

	public void deleteByUserIdAndToken(long userId, String token) {
		userTokenDao.deleteByUserIdAndToken(userId, token);
	}

	public User findUserByToken(String token) {
		User user = null;
		List<OfUserToken> ofUserTokens = userTokenDao.fetchByToken(token);
		if (!ofUserTokens.isEmpty()) {
			long userId = ofUserTokens.get(0).getUserId();
			user = userManager.findById(userId);
		}
		return user;
	}

	private List<UserToken> fill(List<OfUserToken> ofUserTokens) {
		List<UserToken> result = new ArrayList<UserToken>();
		for (OfUserToken ofUserToken : ofUserTokens) {
			result.add(fill(ofUserToken));
		}
		return result;
	}

	private UserToken fill(OfUserToken ofUserToken) {
		User user = userManager.findById(ofUserToken.getUserId());
		return new UserToken(ofUserToken, user);
	}

}
