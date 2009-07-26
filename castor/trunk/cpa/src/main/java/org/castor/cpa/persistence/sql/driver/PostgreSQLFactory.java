/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Intalio, Inc.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Intalio, Inc. Exolab is a registered
 *    trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.castor.cpa.persistence.sql.driver;

import java.sql.Types;

import org.exolab.castor.persist.spi.PersistenceQuery;
import org.exolab.castor.persist.spi.QueryExpression;

/**
 * {@link org.exolab.castor.persist.spi.PersistenceFactory} for postgreSQL JDBC driver.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date: 2006-02-21 16:05:42 -0700 (Tue, 21 Feb 2006) $
 */
public final class PostgreSQLFactory extends GenericFactory {
    //-----------------------------------------------------------------------------------

    /** Internal name for this {@link org.exolab.castor.persist.spi.PersistenceFactory} instance. */
    public static final String FACTORY_NAME = "postgresql";
    
    /**
     * @inheritDoc
     */
    public String getFactoryName() {
        return FACTORY_NAME;
    }

    /**
     * @inheritDoc
     */
    public QueryExpression getQueryExpression() {
        return new PostgreSQLQueryExpression(this);
    }

    /**
     * Determine if the given SQLException is DuplicateKeyException.
     * 
     * @return Boolean.TRUE means "yes",
     *         Boolean.FALSE means "no",
     *         null means "cannot determine".
     */
    public Boolean isDuplicateKeyException(final Exception ex) {
        Boolean isDuplicateKey = Boolean.FALSE;

        if (ex.getMessage().indexOf("duplicate key") > 0) {
            isDuplicateKey = Boolean.TRUE;
        }

        return isDuplicateKey;
    }

    /**
     * @inheritDoc
     */
    public String quoteName(final String name) {
        return doubleQuoteName(name);
    }

    /**
     * Needed to process OQL queries of "CALL" type (using stored procedure
     * call). This feature is specific for JDO.
     * 
     * @param call Stored procedure call (without "{call")
     * @param paramTypes The types of the query parameters
     * @param javaClass The Java class of the query results
     * @param fields The field names
     * @param sqlTypes The field SQL types
     * @return null if this feature is not supported.
     */
    public PersistenceQuery getCallQuery(final String call, final Class<?>[] paramTypes,
            final Class<?> javaClass, final String[] fields, final int[] sqlTypes) {
        return new PostgreSQLCallQuery(call, paramTypes, javaClass, fields, sqlTypes);
    }

    /**
     * @inheritDoc
     * <br/>
     * BLOB/CLOB types are not supported.
     */
    public Class<?> adjustSqlType(final Class<?> sqlType) {
        if (sqlType == java.sql.Clob.class) {
            return java.lang.String.class;
        } else if (sqlType == java.io.InputStream.class) {
            return byte[].class;
        } else {
            return sqlType;
        }
    }
    
    //-----------------------------------------------------------------------------------

    @Override
    public boolean isKeyGeneratorIdentitySupported() {
        return true;
    }
    
    @Override
    public boolean isKeyGeneratorIdentityTypeSupported(final int type) {
        if (type == Types.INTEGER) { return true; }
        if (type == Types.NUMERIC) { return true; }
        if (type == Types.DECIMAL) { return true; }
        if (type == Types.BIGINT) { return true; }
        return false;
    }
    
    @Override
    public String getIdentityQueryString(final String tableName) {
        return "SELECT currval ('" +  tableName + "_id_seq')";
    }
    
    @Override
    public boolean isKeyGeneratorSequenceSupported(final boolean returning, final boolean trigger) {
        return !returning || !trigger;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isKeyGeneratorSequenceTypeSupported(final int type) {
        if (type == Types.INTEGER) { return true; }
        if (type == Types.DECIMAL) { return true; }
        if (type == Types.NUMERIC) { return true; }
        if (type == Types.BIGINT) { return true; }
        if (type == Types.CHAR) { return true; }
        if (type == Types.VARCHAR) { return true; }

        return false;
    }
    
    //-----------------------------------------------------------------------------------
}


