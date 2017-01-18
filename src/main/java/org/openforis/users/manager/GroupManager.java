package org.openforis.users.manager;

import java.util.ArrayList;
import java.util.List;

import org.openforis.users.dao.GroupDao;
import org.openforis.users.jooq.tables.pojos.OfGroup;
import org.openforis.users.model.Group;
import org.openforis.users.model.Group.Visibility;

/**
 * 
 * @author S. Ricci
 *
 */
public class GroupManager extends AbstractManager<Group, GroupDao> {

	public GroupManager(GroupDao dao) {
		super(dao);
	}

	public List<Group> loadEnabledPublicGroups() {
		List<OfGroup> allOfGroups = dao.findAll();
		List<Group> result = new ArrayList<Group>(allOfGroups.size());
		for (OfGroup ofGroup : allOfGroups) {
			Group group = new Group(ofGroup);
			if (group.getEnabled() && group.getVisibility() == Visibility.PUBLIC) {
				result.add(group);
			}
		}
		return result;
	}
	
}
