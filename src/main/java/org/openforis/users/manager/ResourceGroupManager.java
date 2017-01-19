package org.openforis.users.manager;

import java.util.List;

import org.openforis.users.dao.ResourceGroupDao;
import org.openforis.users.jooq.tables.pojos.OfResourceGroup;
import org.openforis.users.model.Group;

/**
 * 
 * @author S. Ricci
 *
 */
public class ResourceGroupManager {

	private ResourceGroupDao resourceGroupDao;
	private GroupManager groupManager;

	public ResourceGroupManager(ResourceGroupDao resourceGroupDao, GroupManager groupManager) {
		super();
		this.resourceGroupDao = resourceGroupDao;
		this.groupManager = groupManager;
	}
	
	public Group loadGroup(String resourceType, String resourceId) {
		Long groupId = resourceGroupDao.loadGroupIdByResource(resourceType, resourceId);
		return groupId == null ? null : groupManager.findById(groupId);
	}
	
	public List<String> loadResourceIds(String resourceType, Group group) {
		return resourceGroupDao.loadResourceIdsByGroup(resourceType, group);
	}

	public int countResourcesByUserId(long userId) {
		return resourceGroupDao.countResourcesByUser(userId);
	}
	
	public void associate(Group group, String resourceType, String resourceId) {
		resourceGroupDao.insert(new OfResourceGroup(resourceType, resourceId, group.getId()));
	}
	
	public void disassociate(Group group, String resourceType, String resourceId) {
		resourceGroupDao.delete(new OfResourceGroup(resourceType, resourceId, group.getId()));
	}

}
