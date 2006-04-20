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
 * Copyright 2000 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id: SQLRelationLoader.java,
 */


package org.exolab.castor.persist;

import org.exolab.castor.persist.spi.Complex;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.loader.Types;
import org.exolab.castor.mapping.xml.ClassMapping;
import org.exolab.castor.mapping.xml.FieldMapping;
import org.exolab.castor.mapping.loader.MappingLoader;
import org.exolab.castor.persist.spi.Persistence;
import org.exolab.castor.persist.spi.PersistenceQuery;
import org.exolab.castor.mapping.loader.RelationDescriptor;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.persist.spi.CallbackInterceptor;
import org.exolab.castor.persist.spi.PersistenceFactory;
import org.exolab.castor.util.Messages;
import org.exolab.castor.jdo.PersistenceException;
import java.util.Vector;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * SQLRelationLoader is a quick hack for creating and removing
 * relation from a many-to-many relation database from ClassMolder.
 * Eventually, it will be merged into SQLEngine. But, it requires
 * chaning of the Persistence interface.
 *
 * @author <a href="mailto:yip@intalio.com">Thomas Yip</a>
 */
public class SQLRelationLoader {

    private String tableName;

    private int[] leftType;

    private int[] rightType;

    private String[] left;

    private String[] right;

    /**
     * the SQL statement for selecting the relation from the relation table.
     */
    private String select;

    /**
     * the SQL statement to insert the a new relation into the relation table.
     */
    private String insert;

    /**
     * the SQL statement to delete an relation from the relation table.
     */
    private String delete;

    /**
     * the SQL statement to delete all the relation assoicate with the left side type
     */
    private String deleteAll;

    public SQLRelationLoader( String table, String[] key, int[] keyType, String[] otherKey, int[] otherKeyType ) {
        tableName = table;
        left = key;
        right = otherKey;
        leftType = keyType;
        rightType = otherKeyType;

        // construct select statement
        StringBuffer sb = new StringBuffer();
        int count = 0;
        sb.append("SELECT ");
        for ( int i=0; i < left.length; i++ ) {
            if ( i > 0 ) sb.append(",");
            sb.append( left[i] );
            count++;
        }
        for ( int i=0; i < right.length; i++ ) {
            sb.append(",");
            sb.append( right[i] );
            count++;
        }
        sb.append(" FROM ");
        sb.append( tableName );
        sb.append(" WHERE ");
        for ( int i=0; i < left.length; i++ ) {
            if ( i > 0 ) sb.append(" AND ");
            sb.append( left[i] );
            sb.append("=?");
        }
        for ( int i=0; i < right.length; i++ ) {
            sb.append(" AND ");
            sb.append( right[i] );
            sb.append("=?");
        }
        select = sb.toString();

        // construct insert statement
        sb = new StringBuffer();
        count = 0;
        sb.append("INSERT INTO ");
        sb.append( tableName );
        sb.append(" (");
        for ( int i=0; i < left.length; i++ ) {
            if ( i > 0 ) sb.append(",");
            sb.append( left[i] );
            count++;
        }
        for ( int i=0; i < right.length; i++ ) {
            sb.append(",");
            sb.append( right[i] );
            count++;
        }
        sb.append(") VALUES (");
        for ( int i=0; i < count; i++ ) {
            if ( i > 0 ) sb.append(",");
            sb.append("?");
        }
        sb.append(")");
        insert = sb.toString();

        // construct delete statement
        sb = new StringBuffer();
        count = 0;
        sb.append("DELETE FROM ");
        sb.append( tableName );
        sb.append(" WHERE ");
        for ( int i=0; i < left.length; i++ ) {
            if ( i > 0 ) sb.append(" AND ");
            sb.append( left[i] );
            sb.append("=?");
        }
        for ( int i=0; i < right.length; i++ ) {
            sb.append(" AND ");
            sb.append( right[i] );
            sb.append("=?");
        }
        delete = sb.toString();

        // construct delete statement for the left side only
        sb = new StringBuffer();
        count = 0;
        sb.append("DELETE FROM ");
        sb.append( tableName );
        sb.append(" WHERE ");
        for ( int i=0; i < left.length; i++ ) {
            if ( i > 0 ) sb.append(" AND ");
            sb.append( left[i] );
            sb.append("=?");
        }
        deleteAll = sb.toString();

    }
    public void createRelation( Connection conn, Object leftValue, Object rightValue )
            throws PersistenceException {

        try {
            int count = 1;
            ResultSet rset;
            PreparedStatement stmt = conn.prepareStatement( select );
            if ( leftType.length > 1 ) {
                Complex left = (Complex) leftValue;
                for ( int i=0; i < left.size(); i++ ) {
                    stmt.setObject( count, left.get(i), leftType[i] );
                    count++;
                }
            } else {
                stmt.setObject( count, leftValue, leftType[0] );
                count++;
            }
            if ( rightType.length > 1 ) {
                Complex right = (Complex) rightValue;
                for ( int i=0; i < right.size(); i++ ) {
                    stmt.setObject( count, right.get(i), rightType[i] );
                    count++;
                }
            } else {
                stmt.setObject( count, rightValue, rightType[0] );
            }
            count = 1;
            rset = stmt.executeQuery();
            if ( ! rset.next() ) {
                stmt = conn.prepareStatement( insert );
                if ( leftType.length > 1 ) {
                    Complex left = (Complex) leftValue;
                    for ( int i=0; i < left.size(); i++ ) {
                        stmt.setObject( count, left.get(i), leftType[i] );
                        count++;
                    }
                } else {
                    stmt.setObject( count, leftValue, leftType[0] );
                    count++;
                }
                if ( rightType.length > 1 ) {
                    Complex right = (Complex) rightValue;
                    for ( int i=0; i < right.size(); i++ ) {
                        stmt.setObject( count, right.get(i), rightType[i] );
                        count++;
                    }
                } else {
                    stmt.setObject( count, rightValue, rightType[0] );
                }
                int r = stmt.executeUpdate();
            }
        } catch ( SQLException e ) {
            e.printStackTrace();
            throw new PersistenceException( e.toString() );
        }
    }
    public void deleteRelation( Connection conn, Object leftValue )
            throws PersistenceException {

        try {
            int count = 1;
            PreparedStatement stmt = conn.prepareStatement( deleteAll );
            if ( leftType.length > 1 ) {
                Complex left = (Complex) leftValue;
                for ( int i=0; i < left.size(); i++ ) {
                    stmt.setObject( count, left.get(i), leftType[i] );
                    count++;
                }
            } else {
                stmt.setObject( count, leftValue, leftType[0] );
            }
            int i = stmt.executeUpdate();
        } catch ( SQLException e ) {
            e.printStackTrace();
            throw new PersistenceException( e.toString() );
        }
    }

    public void deleteRelation( Connection conn, Object leftValue, Object rightValue )
            throws PersistenceException {

        try {
            int count = 1;
            PreparedStatement stmt = conn.prepareStatement( delete );
            if ( leftType.length > 1 ) {
                Complex left = (Complex) leftValue;
                for ( int i=0; i < left.size(); i++ ) {
                    stmt.setObject( count, left.get(i), leftType[i] );
                    count++;
                }
            } else {
                stmt.setObject( count, leftValue, leftType[0] );
                count++;
            }
            if ( rightType.length > 1 ) {
                Complex right = (Complex) rightValue;
                for ( int i=0; i < right.size(); i++ ) {
                    stmt.setObject( count, right.get(i), rightType[i] );
                    count++;
                }
            } else {
                stmt.setObject( count, rightValue, rightType[0] );
            }
            int i = stmt.executeUpdate();
        } catch ( SQLException e ) {
            e.printStackTrace();
            throw new PersistenceException( e.toString() );
        }
    }

}

