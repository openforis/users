/*
 * This file is generated by jOOQ.
*/
package org.openforis.users.jooq;


import javax.annotation.Generated;

import org.jooq.Sequence;
import org.jooq.impl.SequenceImpl;


/**
 * Convenience access to all sequences in OF_USERS
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.0"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Sequences {

    /**
     * The sequence <code>OF_USERS.SYSTEM_SEQUENCE_0FE72C1A_7ECC_44D7_BE87_EDF659EB248F</code>
     */
    public static final Sequence<Long> SYSTEM_SEQUENCE_0FE72C1A_7ECC_44D7_BE87_EDF659EB248F = new SequenceImpl<Long>("SYSTEM_SEQUENCE_0FE72C1A_7ECC_44D7_BE87_EDF659EB248F", OfUsers.OF_USERS, org.jooq.impl.SQLDataType.BIGINT);

    /**
     * The sequence <code>OF_USERS.SYSTEM_SEQUENCE_BBE1D471_57C1_45C6_A7C3_7368FBA2E135</code>
     */
    public static final Sequence<Long> SYSTEM_SEQUENCE_BBE1D471_57C1_45C6_A7C3_7368FBA2E135 = new SequenceImpl<Long>("SYSTEM_SEQUENCE_BBE1D471_57C1_45C6_A7C3_7368FBA2E135", OfUsers.OF_USERS, org.jooq.impl.SQLDataType.BIGINT);
}
