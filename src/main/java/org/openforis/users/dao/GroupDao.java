package org.openforis.users.dao;

import java.util.List;

import org.jooq.Configuration;
import org.jooq.impl.DSL;
import static org.openforis.users.jooq.tables.OfGroup.*;
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

	public List<Group> loadPublicUserDefinedGroups() {
		return DSL.using(configuration())
			.selectFrom(OF_GROUP)
			.where(OF_GROUP.SYSTEM_DEFINED.isFalse()
				.and(OF_GROUP.VISIBILITY.eq(Visibility.PUBLIC.getCode())))
			.fetchInto(Group.class);
	}
	
}
