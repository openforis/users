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

	public List<Group> findAll(SearchParameters searchParameters) {
		return dao.loadAll(searchParameters);
	}
	
	public static class SearchParameters {
		
		private Boolean enabled;
		private Boolean systemDefined;
		private Visibility visibility;
		private String name;

		public Boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(Boolean enabled) {
			this.enabled = enabled;
		}

		public Boolean isSystemDefined() {
			return systemDefined;
		}

		public void setSystemDefined(Boolean systemDefined) {
			this.systemDefined = systemDefined;
		}

		public Visibility getVisibility() {
			return visibility;
		}

		public void setVisibility(Visibility visibility) {
			this.visibility = visibility;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
	}
	
}
