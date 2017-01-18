package org.openforis.users.dao;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Table;
import org.openforis.users.model.IdentifiableObject;

public class Daos {

	public static void insertAndSetId(DSLContext dsl, Table<?> table, Field<?> idField, IdentifiableObject item) {
		Record record = dsl.newRecord(table, item);
		Result<?> result = dsl.insertInto(table)
			.set(record)
			.returning(idField)
			.fetch();
		item.setId((Long) result.getValue(0, idField));
	}

}
