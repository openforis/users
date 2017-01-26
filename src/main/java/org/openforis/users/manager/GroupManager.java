package org.openforis.users.manager;

import java.util.List;

import org.openforis.users.dao.GroupDao;
import org.openforis.users.model.Group;
import org.openforis.users.model.Group.Visibility;

/**
 * 
 * @author S. Ricci
 *
 */
public class GroupManager extends AbstractManager<Group, GroupDao> {

	public GroupManager(GroupDao dao) {
		super(dao, Group.class);
	}

	public void deleteByUserId(Long userId) {
		dao.deleteByUserId(userId);
	}

	public List<Group> findAll(boolean enabled, boolean systemDefined, Visibility visibility) {
		return dao.loadAll(enabled, systemDefined, visibility);
	}
	
}
