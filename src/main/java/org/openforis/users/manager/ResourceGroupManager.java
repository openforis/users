package org.openforis.users.manager;

import java.util.List;
import java.util.Set;

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
	
	public List<String> loadResourceIds(String resourceType, long groupId) {
		return resourceGroupDao.loadResourceIdsByGroup(resourceType, groupId);
	}

	public int countResourcesByUserId(long userId) {
		return resourceGroupDao.countResourcesByUser(userId);
	}
	
	public boolean associate(long groupId, String resourceType, String resourceId) {
		resourceGroupDao.insert(new OfResourceGroup(resourceType, resourceId, groupId));
		return true;
	}
	
	public boolean disassociate(long groupId, String resourceType, String resourceId) {
		resourceGroupDao.delete(new OfResourceGroup(resourceType, resourceId, groupId));
		return true;
	}
	
	public boolean saveAssociations(long groupId, String resourceType, Set<String> resourceIds) {
		resourceGroupDao.deleteByGroupAndResourceType(groupId, resourceType);
		resourceGroupDao.insert(groupId, resourceType, resourceIds);
		return true;
	}

}
