package org.openforis.users.manager;

import java.util.List;

import org.openforis.users.dao.ResourceGroupDao;
import org.openforis.users.exception.BadRequestException;
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

	public OfResourceGroup associate(String resourceType, String resourceId, long groupId) throws BadRequestException {
		OfResourceGroup resourceGroup = resourceGroupDao.fetchOne(resourceType, resourceId, groupId);
		if (resourceGroup != null) {
			throw new BadRequestException();
		} else {
			resourceGroup = new OfResourceGroup(resourceType, resourceId, groupId);
			resourceGroupDao.insert(resourceGroup);
		}
		return resourceGroup;
	}

	public void disassociate(String resourceType, String resourceId, long groupId) {
		OfResourceGroup resourceGroup = new OfResourceGroup(resourceType, resourceId, groupId);
		resourceGroupDao.delete(resourceGroup);
	}

}
