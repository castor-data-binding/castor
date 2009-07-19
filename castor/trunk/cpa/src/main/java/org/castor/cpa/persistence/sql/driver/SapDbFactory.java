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
import java.util.StringTokenizer;

import org.exolab.castor.persist.spi.PersistenceQuery;
import org.exolab.castor.persist.spi.QueryExpression;

/**
 * PersistenceFactory for SAP DB.
 *
 * @author <a href="on@ibis.odessa.ua">Oleg Nitz</a>
 * @author <a href="ferret AT frii DOT com">Bruce Snyder</a>
 * @version $Revision$ $Date: 2006-04-13 06:47:36 -0600 (Thu, 13 Apr 2006) $
 */
public final class SapDbFactory extends GenericFactory {
    //-----------------------------------------------------------------------------------

    public static final String FACTORY_NAME = "sapdb";

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
        return new SapDbQueryExpression(this);
    }

    /**
     * @inheritDoc
     */
    public Boolean isDuplicateKeyException(final Exception except) {
        // Sometime gives wrong results
        //if ( except instanceof SQLException )
        //    return ( (SQLException) except ).getErrorCode() == 1 ? Boolean.TRUE : Boolean.FALSE;
        return null;
    }

    /**
     * @inheritDoc
     */
    public String quoteName(final String name) {
        StringBuffer buffer = new StringBuffer();
        StringTokenizer tokens = new StringTokenizer(name, ".");
        String token = null;
        boolean addQuote = true;


        buffer.append('\"');
        buffer.append(tokens.nextToken().toUpperCase());

        while (tokens.hasMoreTokens()) {
            token = tokens.nextToken();

            if (isAFunction(token)) {
                addQuote = false;
                buffer.append("\".");
            } else {
                buffer.append("\".\"");
                token = token.toUpperCase();
            }

            buffer.append(token);
        }

        if (addQuote) {
            buffer.append('\"');
        }

        return buffer.toString();
    }

    /**
     * Tests a text string against a known list of functions to determine if it is a function.
     *
     * @param text The text to be checked.
     * @return <code>true</code> if text is a known function name, <code>false</code>
     *         otherwise.
     */
    private boolean isAFunction(final String text) {
        boolean isAFunction = false;

        // Add all supported functions in SAP DB here
        String[] knownFunctions = new String[] {
            "nextval",
            "currval"
        };

        for (int i = 0; i < knownFunctions.length; ++i) {
            String function = knownFunctions[i];

            if (text.equals(function)) {
                return true;
            }
        }

        return isAFunction;
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
        return new ReturnedRSCallQuery(call, paramTypes, javaClass, fields, sqlTypes);
    }

    /**
     * For INTEGER type ResultSet.getObject() returns BigDecimal:
     * dependent objects with integer identity cause type conversion error
     * (need to fix SimpleQueryExecutor).
     * 
     * @inheritDoc
     */
    public Class<?> adjustSqlType(final Class<?> sqlType) {
        if (sqlType == java.lang.Integer.class) {
            return java.math.BigDecimal.class;
        }
        return sqlType;
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
    
    //-----------------------------------------------------------------------------------
}


