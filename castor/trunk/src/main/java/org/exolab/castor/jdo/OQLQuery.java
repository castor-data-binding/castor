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


package org.exolab.castor.jdo;


/**
 * An OQL query object. Obtained from the database and used to
 * construct and execute a query on that database. All query operations
 * are bound to the database transaction. Closing the database or the
 * transaction will effectively close the query.
 * <p>
 * If the query specified parameters these parameters must be set
 * (bound) before executing the query. Execution of the query will
 * result in an enumeration of all the objects found by the query.
 * The query can be re-executed by binding new parameters and calling
 * the {@link #execute()} method a second time. A query can be
 * re-execute while objects are still retrieved from a previous
 * execution.
 * <p>
 * For example:
 * <pre>
 * Query        oql;
 * QueryResults results;
 *
 * <font color="red">// Construct a new query and bind the id value</font>
 * oql = db.getQuery( "SELECT ... WHERE id=$1" );
 * oql.bind( 5 );
 * results = oql.execute();
 * <font color="red">// Iterate over all the results and print them</font>
 * while ( results.hasMore() ) {
 *   System.out.println( results.next(); );
 * }
 * results.close();
 * oql.close();
 * </pre>
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 * @see Query
 */
public interface OQLQuery
    extends Query
{

    /**
     * Creates an OQL query from the supplied statement.
     *
     * @param query An OQL query statement
     * @throws PersistenceException
     */
    public void create( String query )
        throws PersistenceException;

}
