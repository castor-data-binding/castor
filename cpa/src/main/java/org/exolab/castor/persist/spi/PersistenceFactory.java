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
package org.exolab.castor.persist.spi;

import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.MappingException;

/**
 * Factory for producing new persistence implementations. Used for
 * constructing a persistence service provider (see {@link Persistence})
 * as well as for constructing new query expressions (see {@link
 * QueryExpression}).
 * <p>
 * The factory is specified in the JDO configuration file for the
 * database and is configured through Bean-like accessor methods.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @author <a href="mailto:ferret AT frii dot com">Bruce Snyder</a>
 * @version $Revision$ $Date: 2004-03-25 05:34:48 -0700 (Thu, 25 Mar 2004) $
 * @see Persistence
 */
public interface PersistenceFactory {
    /**
     * Returns the name of this factory. A descriptive name that
     * indicates the type of supported database server or SQL syntax.
     *
     * @return The name of this factory
     */
    String getFactoryName();

    /**
     * Returns a persistence implementation for the specified object
     * type (given its descriptor) on behalf of the specified cache
     * engine. Return null if no persistence support is available for
     * the specified object type.
     *
     * @param clsDesc The class descriptor
     *  (may be null)
     * @return A suiteable persistence implementation, or null
     * @throws MappingException Indicates that the object type is not
     *  supported by the persistence engine due to improper mapping
     */
    Persistence getPersistence(ClassDescriptor clsDesc)
    throws MappingException;

    /**
     * Returns a new empty query expression suitable for the underlying
     * SQL engine. The implementation will construct SQL query
     * statements in the preferred syntax.
     *
     * @return New empty query expression
     */
    QueryExpression getQueryExpression();

    /**
     * Determines it the given exception is indication of a duplicate
     * key.
     *
     * @return TRUE means "yes", FALSE means "no", null means "cannot determine"
     */
    Boolean isDuplicateKeyException(Exception except);

    /**
     * Returns the quoted identifier suitable for preventing conflicts between
     * database identifiers and reserved keywords.
     *
     * @param name The identifier (table, column, etc)
     * @return The quoted identifier
     */
    String quoteName(String name);

    /**
     * Some databases has some problems with some SQL types.
     * Usually it is enough to merely replace one SQL type by another.
     * 
     * @param sqlType The correspondent Java class for the SQL type in mapping.xml
     * @return The correspondent Java class for the SQL type that should be used instead.
     */
    Class adjustSqlType(Class sqlType);

    /**
     * Many databases don't support setNull for "WHERE fld=?" and require "WHERE fld IS NULL".
     */
    boolean supportsSetNullInWhere();

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
    PersistenceQuery getCallQuery(String call, Class[] paramTypes,
            Class javaClass, String[] fields, int[] sqlTypes);
}

