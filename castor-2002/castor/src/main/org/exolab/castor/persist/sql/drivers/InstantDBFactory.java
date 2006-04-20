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


package org.exolab.castor.persist.sql.drivers;

import java.sql.SQLException;
import org.exolab.castor.persist.spi.QueryExpression;
import org.exolab.castor.persist.spi.PersistenceQuery;

/**
  *  Persistence factory for InstantDB database (<a href="http://instantdb.enhydra.org/">http://instantdb.enhydra.org/</a> ). 
  *  <p>
  *  Example <code>database.xml</code> file for JDO
  *  <br>
  *  <pre>
  *   &lt;database name="test" engine="instantdb" &gt;
  *       &lt;driver class-name="org.enhydra.instantdb.jdbc.idbDriver" 
  *               url="jdbc:idb:C:\\castor-0.8.8\\db\\test\\test.prp"&gt;
  *         &lt;param name="user" value="" /&gt;
  *         &lt;param name="password" value="" /&gt;
  *       &lt;/driver&gt;
  *       &lt;mapping href="mapping.xml" /&gt;
  *    &lt;/database&gt;
  *  </pre> 
  *
  *
  *  @author <a href="mailto:bozyurt@san.rr.com">I. Burak Ozyurt</a>
  *  @version 1.0
  */
public class InstantDBFactory extends GenericFactory {


  public String getFactoryName(){
        return "instantdb";
  }


  public QueryExpression getQueryExpression() {
        return new InstantDBQueryExpression( this );
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
        // stored procedures are not supported by Instant DB
        return null;
    }


    /**
     * For NUMERIC type ResultSet.getObject() returns Double instead of
     * BigDecimal for InstantDB.
     */
    public Class adjustSqlType( Class sqlType ) {
        if (sqlType == java.math.BigDecimal.class) {
            return java.lang.Double.class;
        } else {
            return sqlType;
        }
    }

}
