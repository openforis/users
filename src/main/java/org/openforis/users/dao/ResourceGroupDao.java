package org.openforis.users.dao;

import static org.openforis.users.jooq.tables.OfResourceGroup.OF_RESOURCE_GROUP;

import java.util.Arrays;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.openforis.users.jooq.tables.daos.OfResourceGroupDao;
import org.openforis.users.model.Group;

/**
 * 
 * @author S. Ricci
 *
 */
public class ResourceGroupDao extends OfResourceGroupDao {

	public List<String> loadResourceIdsByGroup(String resourceType, Group group) {
		String[] result = dsl().select(OF_RESOURCE_GROUP.RESOURCE_ID)
			.from(OF_RESOURCE_GROUP)
			.where(OF_RESOURCE_GROUP.RESOURCE_TYPE.eq(resourceType))
			.fetchArray(OF_RESOURCE_GROUP.RESOURCE_ID);
		return Arrays.asList(result);
	}

	public Long loadGroupIdByResource(String resourceType, String resourceId) {
		return dsl().select(OF_RESOURCE_GROUP.GROUP_ID)
				.from(OF_RESOURCE_GROUP)
				.where(OF_RESOURCE_GROUP.RESOURCE_TYPE.eq(resourceType)
					.and(OF_RESOURCE_GROUP.RESOURCE_ID.eq(resourceId)))
				.fetchOne(OF_RESOURCE_GROUP.GROUP_ID);
	}
	
	private DSLContext dsl() {
		return DSL.using(configuration());
	}
}
