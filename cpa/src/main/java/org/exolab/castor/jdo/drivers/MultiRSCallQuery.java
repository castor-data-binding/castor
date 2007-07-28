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

import java.sql.Connection;
import java.sql.SQLException;

import org.castor.util.Messages;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryException;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.persist.spi.AbstractCallQuery;

/**
 * PersistenceQuery implementation for CallableStatements
 * that yield multiple ResultSets, like Sybase stored procedures.
 *
 * @author <a href="on@ibis.odessa.ua">Oleg Nitz</a>
 * @version $Revision$ $Date: 2006-04-11 15:26:07 -0600 (Tue, 11 Apr 2006) $
 */
final class MultiRSCallQuery extends AbstractCallQuery
{
    /**
     * Creates an instance of this clas.
     * @param call The SQL CALL statement to execute
     * @param types Java types of the parameters
     * @param javaClass Class type of the result
     * @param fields ???
     * @param sqlTypes SQL types of the parameters
     */
    MultiRSCallQuery(final String call, 
            final Class[] types, 
            final Class javaClass,
            final String[] fields, 
            final int[] sqlTypes) {
        super(call, types, javaClass, sqlTypes);
    }

    protected void execute( Object conn, AccessMode accessMode )
        throws QueryException, PersistenceException
    {
        _lastIdentity = null;
        try {
            _stmt = ( (Connection) conn ).prepareCall( _call );
            for ( int i = 0 ; i < _values.length ; ++i ) {
                _stmt.setObject( i + 1, _values[ i ] );
                _values[ i ] = null;
            }
            _stmt.execute();
            _rs = _stmt.getResultSet();
        } catch ( SQLException except ) {
            throw new PersistenceException( Messages.format( "persist.nested", except ) );
        }
    }


    protected boolean nextRow() throws SQLException {
        while ( true ) {
            if ( _rs != null && _rs.next() ) {
                return true;
            }
            if ( !_stmt.getMoreResults() && _stmt.getUpdateCount() == -1 ) {
                _rs = null;
                return false;
            }
            _rs = _stmt.getResultSet();
        }
    }

}
