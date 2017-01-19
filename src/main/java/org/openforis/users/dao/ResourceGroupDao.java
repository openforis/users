package org.openforis.users.dao;

import static org.openforis.users.jooq.tables.OfResourceGroup.OF_RESOURCE_GROUP;
import static org.openforis.users.jooq.tables.OfUserGroup.OF_USER_GROUP;

import java.util.Arrays;
import java.util.List;

import org.jooq.AggregateFunction;
import org.jooq.Configuration;
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

	public ResourceGroupDao(Configuration configuration) {
		super(configuration);
	}

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
	
	public int countResourcesByUser(long userId) {
		DSLContext dsl = dsl();
		AggregateFunction<Integer> resourceCountFunction = DSL.count(OF_RESOURCE_GROUP.RESOURCE_ID);
		
		int userResourceCount = dsl.select(resourceCountFunction).from(OF_RESOURCE_GROUP).where(
				DSL.exists(
						dsl.select(OF_USER_GROUP.GROUP_ID)
						.from(OF_USER_GROUP)
						.where(OF_USER_GROUP.USER_ID.eq(userId)
								.and(OF_USER_GROUP.GROUP_ID.eq(OF_RESOURCE_GROUP.GROUP_ID))
								)
						)
				).fetchOne(resourceCountFunction);
		return userResourceCount;
	}
	
	private DSLContext dsl() {
		return DSL.using(configuration());
	}
}
