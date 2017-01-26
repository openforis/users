package org.openforis.users.dao;

import java.util.List;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import static org.openforis.users.jooq.tables.OfGroup.*;
import static org.openforis.users.jooq.tables.OfUserGroup.OF_USER_GROUP;

import org.openforis.users.jooq.tables.daos.OfGroupDao;
import org.openforis.users.model.Group;
import org.openforis.users.model.Group.Visibility;

/**
 * 
 * @author S. Ricci
 *
 */
public class GroupDao extends OfGroupDao {

	public GroupDao(Configuration config) {
		super(config);
	}

	public List<Group> loadAll(boolean enabled, boolean systemDefined, Visibility visibility) {
		return dsl()
			.selectFrom(OF_GROUP)
			.where(OF_GROUP.ENABLED.eq(enabled)
				.and(OF_GROUP.SYSTEM_DEFINED.eq(systemDefined))
				.and(OF_GROUP.VISIBILITY_CODE.eq(visibility.getCode())))
			.fetchInto(Group.class);
	}

	public void deleteByUserId(Long userId) {
		dsl().deleteFrom(OF_USER_GROUP)
			.where(OF_USER_GROUP.USER_ID.eq(userId))
			.execute();
	}
	
	private DSLContext dsl() {
		return DSL.using(configuration());
	}

}
