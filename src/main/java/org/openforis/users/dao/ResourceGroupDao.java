package org.openforis.users.dao;

import static org.openforis.users.jooq.tables.OfResourceGroup.OF_RESOURCE_GROUP;
import static org.openforis.users.jooq.tables.OfUserGroup.OF_USER_GROUP;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.jooq.AggregateFunction;
import org.jooq.BatchBindStep;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.openforis.users.jooq.tables.daos.OfResourceGroupDao;

/**
 * 
 * @author S. Ricci
 *
 */
public class ResourceGroupDao extends OfResourceGroupDao {

	public ResourceGroupDao(Configuration configuration) {
		super(configuration);
	}

	public List<String> loadResourceIdsByGroup(String resourceType, long groupId) {
		String[] result = dsl().select(OF_RESOURCE_GROUP.RESOURCE_ID)
			.from(OF_RESOURCE_GROUP)
			.where(OF_RESOURCE_GROUP.RESOURCE_TYPE.eq(resourceType)
					.and(OF_RESOURCE_GROUP.GROUP_ID.eq(groupId)))
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
	
	public int deleteByGroupAndResourceType(long groupId, String resourceType) {
		return dsl().deleteFrom(OF_RESOURCE_GROUP).where(
				OF_RESOURCE_GROUP.GROUP_ID.eq(groupId)
					.and(OF_RESOURCE_GROUP.RESOURCE_TYPE.eq(resourceType))
		).execute();
	}
	
	public void insert(long groupId, String resourceType, Set<String> resourceIds) {
		DSLContext dsl = dsl();
		BatchBindStep batch = dsl.batch(dsl.insertInto(OF_RESOURCE_GROUP)
				.columns(OF_RESOURCE_GROUP.GROUP_ID,
						OF_RESOURCE_GROUP.RESOURCE_TYPE, 
						OF_RESOURCE_GROUP.RESOURCE_ID)
				);
		for (String resourceId : resourceIds) {
			batch.bind(groupId, resourceType, resourceId);
		}
		batch.execute();
	}
	
	private DSLContext dsl() {
		return DSL.using(configuration());
	}
}
