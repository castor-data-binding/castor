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
 *    permission of Exoffice Technologies.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Exoffice Technologies. Exolab is a registered
 *    trademark of Exoffice Technologies.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY EXOFFICE TECHNOLOGIES AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * EXOFFICE TECHNOLOGIES OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Exoffice Technologies Inc. All Rights Reserved.
 *
 * $Id$
 */


package org.exolab.castor.jdo;


import java.util.Enumeration;


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
 * the {@link #execute} method a second time. A query can be
 * re-execute while objects are still retrieved from a previous
 * execution.
 * <p>
 * For example:
 * <pre>
 * OQLQuery    oql;
 * Enumeration enum;
 *
 * <font color="red">// Construct a new query and bind the id value</font>
 * oql = db.getOQLQuery( "SELECT ... WHERE id=$" );
 * oql.bind( 5 );
 * enum = oql.execute();
 * <font color="red">// Iterate over all the results and print them</font>
 * while ( enum.hasMoreElements() ) {
 *   System.out.println( enum.nextElement(); );
 * }
 * </pre>
 *
 * @author <a href="arkin@exoffice.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 * @see Database#getOQLQuery
 */
public interface OQLQuery
{


    /**
     * Creates an OQL query from the supplied statement.
     *
     * @param query An OQL query statement
     * @throws QueryException The query syntax is invalid
     */
    public void create( String query )
        throws QueryException;


    /**
     * Bind a parameter value to the query. Parameters are set in the
     * order in which they appear in the query and must match in number
     * and type of each parameter.
     *
     * @param value The parameter value
     * @throws IllegalArgumentException The parameter is not of the
     *  expected type, or more parameters were supplied that the
     *  query specified
     */
    public void bind( Object value )
      throws IllegalArgumentException;
    

    /**
     * Bind a parameter value to the query. Parameters are set in the
     * order in which they appear in the query and must match in number
     * and type of each parameter.
     *
     * @param value The parameter value
     * @throws IllegalArgumentException The parameter is not of the
     *  expected type, or more parameters were supplied that the
     *  query specified
     */
    public void bind( boolean value )
      throws IllegalArgumentException;
    

    /**
     * Bind a parameter value to the query. Parameters are set in the
     * order in which they appear in the query and must match in number
     * and type of each parameter.
     *
     * @param value The parameter value
     * @throws IllegalArgumentException The parameter is not of the
     *  expected type, or more parameters were supplied that the
     *  query specified
     */
    public void bind( short value )
      throws IllegalArgumentException;
    

    /**
     * Bind a parameter value to the query. Parameters are set in the
     * order in which they appear in the query and must match in number
     * and type of each parameter.
     *
     * @param value The parameter value
     * @throws IllegalArgumentException The parameter is not of the
     *  expected type, or more parameters were supplied that the
     *  query specified
     */
    public void bind( int value )
      throws IllegalArgumentException;
    

    /**
     * Bind a parameter value to the query. Parameters are set in the
     * order in which they appear in the query and must match in number
     * and type of each parameter.
     *
     * @param value The parameter value
     * @throws IllegalArgumentException The parameter is not of the
     *  expected type, or more parameters were supplied that the
     *  query specified
     */
    public void bind( long value )
      throws IllegalArgumentException;
    

    /**
     * Bind a parameter value to the query. Parameters are set in the
     * order in which they appear in the query and must match in number
     * and type of each parameter.
     *
     * @param value The parameter value
     * @throws IllegalArgumentException The parameter is not of the
     *  expected type, or more parameters were supplied that the
     *  query specified
     */
    public void bind( String value )
      throws IllegalArgumentException;
    

    /**
     * Bind a parameter value to the query. Parameters are set in the
     * order in which they appear in the query and must match in number
     * and type of each parameter.
     *
     * @param value The parameter value
     * @throws IllegalArgumentException The parameter is not of the
     *  expected type, or more parameters were supplied that the
     *  query specified
     */
    public void bind( float value )
      throws IllegalArgumentException;
    

    /**
     * Bind a parameter value to the query. Parameters are set in the
     * order in which they appear in the query and must match in number
     * and type of each parameter.
     *
     * @param value The parameter value
     * @throws IllegalArgumentException The parameter is not of the
     *  expected type, or more parameters were supplied that the
     *  query specified
     */
    public void bind( double value )
      throws IllegalArgumentException;


    /**
     * Execute the query. The query is executed returning an enumeration
     * of all the objects found. If no objects were found, the
     * enumeration will be empty.
     * <p>
     * After execution the parameter list is reset. New parameters can
     * be bound and the query re-executed.
     *
     * @return An enumeration of all objects found
     * @throws QueryException The query expression cannot be processed,
     *  or the query parameters are invalid
     * @throws TransactionNotInProgressException Method called while
     *   transaction is not in progress
     * @throws PersistenceException An error reported by the
     *  persistence engine
     */
    public Enumeration execute()
        throws QueryException, PersistenceException, TransactionNotInProgressException;


    /**
     * <b>Experimental</b>
     * <p>
     * Execute the query. The query is executed returning an enumeration
     * of all the objects found. If no objects were found, the
     * enumeration will be empty.
     * <p>
     * After execution the parameter list is reset. New parameters can
     * be bound and the query re-executed.
     *
     * @param accessMode The access mode
     * @return An enumeration of all objects found
     * @throws QueryException The query expression cannot be processed,
     *  or the query parameters are invalid
     * @throws TransactionNotInProgressException Method called while
     *   transaction is not in progress
     * @throws PersistenceException An error reported by the
     *  persistence engine
     */
    public Enumeration execute( short accessMode )
        throws QueryException, PersistenceException, TransactionNotInProgressException;


}

