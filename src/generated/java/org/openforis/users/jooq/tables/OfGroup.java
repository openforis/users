/*
 * This file is generated by jOOQ.
*/
package org.openforis.users.jooq.tables;


import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Identity;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.TableImpl;
import org.openforis.users.jooq.Keys;
import org.openforis.users.jooq.OfUsers;
import org.openforis.users.jooq.tables.records.OfGroupRecord;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.0"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class OfGroup extends TableImpl<OfGroupRecord> {

    private static final long serialVersionUID = -180565655;

    /**
     * The reference instance of <code>OF_USERS.OF_GROUP</code>
     */
    public static final OfGroup OF_GROUP = new OfGroup();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<OfGroupRecord> getRecordType() {
        return OfGroupRecord.class;
    }

    /**
     * The column <code>OF_USERS.OF_GROUP.ID</code>.
     */
    public final TableField<OfGroupRecord, Long> ID = createField("ID", org.jooq.impl.SQLDataType.BIGINT.nullable(false).defaultValue(org.jooq.impl.DSL.field("(NEXT VALUE FOR OF_USERS.SYSTEM_SEQUENCE_4825A3E2_7F19_413F_9FA1_CC095355F412)", org.jooq.impl.SQLDataType.BIGINT)), this, "");

    /**
     * The column <code>OF_USERS.OF_GROUP.NAME</code>.
     */
    public final TableField<OfGroupRecord, String> NAME = createField("NAME", org.jooq.impl.SQLDataType.VARCHAR.length(255).nullable(false), this, "");

    /**
     * The column <code>OF_USERS.OF_GROUP.LABEL</code>.
     */
    public final TableField<OfGroupRecord, String> LABEL = createField("LABEL", org.jooq.impl.SQLDataType.VARCHAR.length(255).nullable(false), this, "");

    /**
     * The column <code>OF_USERS.OF_GROUP.DESCRIPTION</code>.
     */
    public final TableField<OfGroupRecord, String> DESCRIPTION = createField("DESCRIPTION", org.jooq.impl.SQLDataType.VARCHAR.length(1000), this, "");

    /**
     * The column <code>OF_USERS.OF_GROUP.ENABLED</code>.
     */
    public final TableField<OfGroupRecord, Boolean> ENABLED = createField("ENABLED", org.jooq.impl.SQLDataType.BOOLEAN.nullable(false).defaultValue(org.jooq.impl.DSL.field("FALSE", org.jooq.impl.SQLDataType.BOOLEAN)), this, "");

    /**
     * The column <code>OF_USERS.OF_GROUP.SYSTEM_DEFINED</code>.
     */
    public final TableField<OfGroupRecord, Boolean> SYSTEM_DEFINED = createField("SYSTEM_DEFINED", org.jooq.impl.SQLDataType.BOOLEAN.nullable(false).defaultValue(org.jooq.impl.DSL.field("FALSE", org.jooq.impl.SQLDataType.BOOLEAN)), this, "");

    /**
     * The column <code>OF_USERS.OF_GROUP.VISIBILITY_CODE</code>. PUB=Public, PRV=Private
     */
    public final TableField<OfGroupRecord, String> VISIBILITY_CODE = createField("VISIBILITY_CODE", org.jooq.impl.SQLDataType.CHAR.length(3).nullable(false).defaultValue(org.jooq.impl.DSL.field("'PUB'", org.jooq.impl.SQLDataType.CHAR)), this, "PUB=Public, PRV=Private");

    /**
     * The column <code>OF_USERS.OF_GROUP.LOGO</code>.
     */
    public final TableField<OfGroupRecord, byte[]> LOGO = createField("LOGO", org.jooq.impl.SQLDataType.BLOB, this, "");

    /**
     * The column <code>OF_USERS.OF_GROUP.URL</code>.
     */
    public final TableField<OfGroupRecord, String> URL = createField("URL", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "");

    /**
     * The column <code>OF_USERS.OF_GROUP.CREATION_DATE</code>.
     */
    public final TableField<OfGroupRecord, Timestamp> CREATION_DATE = createField("CREATION_DATE", org.jooq.impl.SQLDataType.TIMESTAMP, this, "");

    /**
     * The column <code>OF_USERS.OF_GROUP.LOGO_CONTENT_TYPE</code>.
     */
    public final TableField<OfGroupRecord, String> LOGO_CONTENT_TYPE = createField("LOGO_CONTENT_TYPE", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "");

    /**
     * Create a <code>OF_USERS.OF_GROUP</code> table reference
     */
    public OfGroup() {
        this("OF_GROUP", null);
    }

    /**
     * Create an aliased <code>OF_USERS.OF_GROUP</code> table reference
     */
    public OfGroup(String alias) {
        this(alias, OF_GROUP);
    }

    private OfGroup(String alias, Table<OfGroupRecord> aliased) {
        this(alias, aliased, null);
    }

    private OfGroup(String alias, Table<OfGroupRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return OfUsers.OF_USERS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<OfGroupRecord, Long> getIdentity() {
        return Keys.IDENTITY_OF_GROUP;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<OfGroupRecord> getPrimaryKey() {
        return Keys.PK_OF_GROUP;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<OfGroupRecord>> getKeys() {
        return Arrays.<UniqueKey<OfGroupRecord>>asList(Keys.PK_OF_GROUP);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OfGroup as(String alias) {
        return new OfGroup(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public OfGroup rename(String name) {
        return new OfGroup(name, null);
    }
}
