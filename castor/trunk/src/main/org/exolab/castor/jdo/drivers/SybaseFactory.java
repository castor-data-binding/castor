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


package org.exolab.castor.jdo.drivers;


import org.exolab.castor.persist.spi.PersistenceQuery;
import org.exolab.castor.persist.spi.QueryExpression;


/**
 * PersistenceFactory for Sybase Adaptive Servers.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @author <a href="on@ibis.odessa.ua">Oleg Nitz</a>
 * @version $Revision$ $Date$
 */
public class SybaseFactory
    extends GenericFactory
{

    public static final String FACTORY_NAME = "sybase";

    /**
     * @inheritDoc
     * @see org.exolab.castor.persist.spi.PersistenceFactory#getFactoryName()
     */
    public String getFactoryName()
    {
        return FACTORY_NAME;
    }

    /**
     * @inheritDoc
     * @see org.exolab.castor.persist.spi.PersistenceFactory#getQueryExpression()
     */
    public QueryExpression getQueryExpression()
    {
        return new SybaseQueryExpression( this );
    }

    /**
     * @inheritDoc
     * @see org.exolab.castor.persist.spi.PersistenceFactory#isDuplicateKeyException(java.lang.Exception)
     */
    public Boolean isDuplicateKeyException( Exception except )
    {
        // Sometime gives wrong results
        //if ( except instanceof SQLException )
        //    switch ( ( (SQLException) except ).getErrorCode() ) {
        //        case 2601: 
        //        case 548:  // sometimes Sybase ASA generates this code instead of 2601
        //            return Boolean.TRUE;
        //        default:
        //            return Boolean.FALSE;
        //    }
        return null;
    }

    /**
     * @inheritDoc
     * @see org.exolab.castor.persist.spi.PersistenceFactory#quoteName(java.lang.String)
     */
    public String quoteName(String name)
    {
        return doubleQuoteName(name);
    }

    /**
     * Needed to process OQL queries of "CALL" type (using stored procedure
     * call). This feature is specific for JDO.
     * @param call Stored procedure call (without "{call")
     * @param paramTypes The types of the query parameters
     * @param javaClass The Java class of the query results
     * @param fields The field names
     * @param sqlTypes The field SQL types
     * @return null if this feature is not supported.
     */
    public PersistenceQuery getCallQuery( String call, Class[] paramTypes, Class javaClass,
                                          String[] fields, int[] sqlTypes )
    {
        return new MultiRSCallQuery( call, paramTypes, javaClass, fields, sqlTypes );
    }

    /**
     * @inheritDoc
     * @see org.exolab.castor.jdo.engine.BaseFactory#supportsSetNullInWhere()
     */
    public boolean supportsSetNullInWhere()
    {
        return true;
    }
}


