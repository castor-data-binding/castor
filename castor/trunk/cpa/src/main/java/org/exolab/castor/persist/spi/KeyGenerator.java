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

import java.sql.Connection;
import java.util.Properties;

import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.mapping.MappingException;

/**
 * Interface for a key generator. The key generator is used for
 * producing identities for objects before they are created in the
 * database.
 * <p>
 * All the key generators belonging to the same database share the
 * same non-transactional connection to the database.
 * <p>
 * The key generator is configured from the mapping file using
 * Bean-like accessor methods.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @author <a href="on@ibis.odessa.ua">Oleg Nitz</a>
 * @author <a href="bruce DOT snyder AT gmail DOT com">Bruce Snyder</a>
 * @version $Revision$ $Date: 2005-04-25 15:33:21 -0600 (Mon, 25 Apr 2005) $
 */
public interface KeyGenerator
{
    /**
     * For the key generators of BEFORE_INSERT style {@link #generateKey}
     * is called before INSERT.
     * {@link #patchSQL} may be used but usually doesn't.
     */
    public static final byte BEFORE_INSERT = -1;


    /**
     * For the key generators of DURING_INSERT style {@link #generateKey}
     * is never called, all work is done by {@link #patchSQL}.
     */
    public static final byte DURING_INSERT = 0;


    /**
     * For the key generators of AFTER_INSERT style {@link #generateKey}
     * is called after INSERT.
     * {@link #patchSQL} may be used but usually doesn't.
     */
    public static final byte AFTER_INSERT = 1;


    /**
     * Generate a new key for the specified table. This method is
     * called when a new object is about to be created. In some
     * environments the name of the owner of the object is known,
     * e.g. the principal in a J2EE server.
     * This method is never called for DURING_INSERT key generators.
     *
     * @param conn An open connection within the given transaction
     * @param tableName The table name
     * @param primKeyName The primary key name
     * @param props A temporary replacement for Principal object
     * @return A new key
     * @throws PersistenceException An error occured talking to persistent
     *  storage
     */
    public Object generateKey( Connection conn, String tableName,
            String primKeyName, Properties props )
        throws PersistenceException;


    /**
     * Determine if the key generator supports a given sql type.
     *
     * @param sqlType
     * @throws MappingException
     */
    public void supportsSqlType( int sqlType )
        throws MappingException;

    /**
     * Style of the key generator: BEFORE_INSERT, DURING_INSERT or AFTER_INSERT.
     */
    public byte getStyle();


    /**
     * Gives a possibility to patch the Castor-generated SQL statement
     * for INSERT (indended mainly for DURING_INSERT style of key generators, 
     * other key generators usually simply return the passed parameter).
     * The original statement contains primary key column on the first place
     * for BEFORE_INSERT style and doesn't contain it for the other styles.
     * This method is called once for each class and must return String 
     * with '?' that can be passed to CallableStatement (for DURING_INSERT 
     * style) or to PreparedStatement (for the others).
     * Then for each record being created actual field values are substituted, 
     * starting from the primary key value for BEFORE_INSERT style, of starting
     * from the first of other fields for the other styles.
     * The DURING_INSERT key generator must add one OUT parameter to the end
     * of the parameter list, which will return the generated identity.
     * For example, ReturningKeyGenerator for Oracle8i transforms
     * "INSERT INTO tbl (pk, fld1, ...,fldN)  VALUES (?,?...,?)" to
     * "INSERT INTO tbl (pk, fld1, ...) VALUES (seq.nextval,?....,?)
     * RETURNING pk INTO ?".
     * DURING_INSERT key generator also may be implemented as a stored procedure.
     * @param insert Castor-generated INSERT statement
     * @param primKeyName The primary key name
     */
    public String patchSQL( String insert, String primKeyName )
            throws MappingException;


    /**
     * Is key generated in the same connection as INSERT?
     * For DURING_INSERT style this method is never called.
     */
    public boolean isInSameConnection();
}
