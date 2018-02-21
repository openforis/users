package org.openforis.users.dao;

import static org.openforis.users.jooq.tables.OfGroup.OF_GROUP;
import static org.openforis.users.jooq.tables.OfUserGroup.OF_USER_GROUP;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Record7;
import org.jooq.SelectJoinStep;
import org.jooq.impl.DSL;
import org.openforis.users.jooq.tables.daos.OfGroupDao;
import org.openforis.users.manager.GroupManager.SearchParameters;
import org.openforis.users.model.Group;

/**
 * 
 * @author S. Ricci
 * @author R. Fontanarosa
 *
 */
public class GroupDao extends OfGroupDao {

	public GroupDao(Configuration config) {
		super(config);
	}

	public List<Group> loadAll(SearchParameters params) {
		SelectJoinStep<Record7<String, String, String, Boolean, Boolean, String, Timestamp>> query = dsl()
				.select(OF_GROUP.NAME, OF_GROUP.LABEL, OF_GROUP.DESCRIPTION,
						OF_GROUP.ENABLED, OF_GROUP.SYSTEM_DEFINED,
						OF_GROUP.VISIBILITY_CODE, OF_GROUP.CREATION_DATE)
				.from(OF_GROUP);
		if (params != null) {
			List<Condition> conditions = new ArrayList<>();
			if (params.getName() != null) {
				conditions.add(OF_GROUP.NAME.eq(params.getName()));
			}
			if (params.isSystemDefined() != null) {
				conditions.add(OF_GROUP.SYSTEM_DEFINED.eq(params.isSystemDefined()));
			}
			if (params.isEnabled() != null) {
				conditions.add(OF_GROUP.ENABLED.eq(params.isEnabled()));
			}
			if (params.getVisibility() != null) {
				conditions.add(OF_GROUP.VISIBILITY_CODE.eq(params.getVisibility().getCode()));
			}
			query.where(conditions);
		}
		return query.fetchInto(Group.class);
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
