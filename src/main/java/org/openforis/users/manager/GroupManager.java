package org.openforis.users.manager;

import java.util.List;

import org.openforis.users.dao.GroupDao;
import org.openforis.users.model.Group;

/**
 * 
 * @author S. Ricci
 *
 */
public class GroupManager extends AbstractManager<Group, GroupDao> {

	public GroupManager(GroupDao dao) {
		super(dao, Group.class);
	}

	public List<Group> loadEnabledPublicGroups() {
		return dao.loadEnabledPublicUserDefinedGroups();
	}

	public void deleteByUserId(Long userId) {
		dao.deleteByUserId(userId);
	}
	
}
