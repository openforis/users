/*
 * This file is generated by jOOQ.
*/
package org.openforis.users.jooq.tables.daos;


import java.util.List;

import javax.annotation.Generated;

import org.jooq.Configuration;
import org.jooq.Record3;
import org.jooq.impl.DAOImpl;
import org.openforis.users.jooq.tables.OfResourceGroup;
import org.openforis.users.jooq.tables.records.OfResourceGroupRecord;


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
public class OfResourceGroupDao extends DAOImpl<OfResourceGroupRecord, org.openforis.users.jooq.tables.pojos.OfResourceGroup, Record3<String, String, Long>> {

    /**
     * Create a new OfResourceGroupDao without any configuration
     */
    public OfResourceGroupDao() {
        super(OfResourceGroup.OF_RESOURCE_GROUP, org.openforis.users.jooq.tables.pojos.OfResourceGroup.class);
    }

    /**
     * Create a new OfResourceGroupDao with an attached configuration
     */
    public OfResourceGroupDao(Configuration configuration) {
        super(OfResourceGroup.OF_RESOURCE_GROUP, org.openforis.users.jooq.tables.pojos.OfResourceGroup.class, configuration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Record3<String, String, Long> getId(org.openforis.users.jooq.tables.pojos.OfResourceGroup object) {
        return compositeKeyRecord(object.getResourceType(), object.getResourceId(), object.getGroupId());
    }

    /**
     * Fetch records that have <code>RESOURCE_TYPE IN (values)</code>
     */
    public List<org.openforis.users.jooq.tables.pojos.OfResourceGroup> fetchByResourceType(String... values) {
        return fetch(OfResourceGroup.OF_RESOURCE_GROUP.RESOURCE_TYPE, values);
    }

    /**
     * Fetch records that have <code>RESOURCE_ID IN (values)</code>
     */
    public List<org.openforis.users.jooq.tables.pojos.OfResourceGroup> fetchByResourceId(String... values) {
        return fetch(OfResourceGroup.OF_RESOURCE_GROUP.RESOURCE_ID, values);
    }

    /**
     * Fetch records that have <code>GROUP_ID IN (values)</code>
     */
    public List<org.openforis.users.jooq.tables.pojos.OfResourceGroup> fetchByGroupId(Long... values) {
        return fetch(OfResourceGroup.OF_RESOURCE_GROUP.GROUP_ID, values);
    }
}