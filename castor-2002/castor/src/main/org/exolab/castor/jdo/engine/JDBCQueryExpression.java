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


package org.exolab.castor.jdo.engine;


import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import org.exolab.castor.persist.spi.QueryExpression;


/**
 *
 * @author <a href="arkin@exoffice.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public class JDBCQueryExpression
    implements QueryExpression
{


    private Hashtable _tables = new Hashtable();


    private Vector    _cols = new Vector();


    private Vector    _conds = new Vector();


    private Vector    _joins = new Vector();


    public void addColumn( String tableName, String columnName )
    {
        _tables.put( tableName, tableName );
        _cols.addElement( tableName + JDBCSyntax.TableColumnSeparator + columnName  );
    }


    public void addParameter( String tableName, String columnName, String condOp )
    {
        addCondition( tableName, columnName, condOp, JDBCSyntax.Parameter );
    }


    public void addCondition( String tableName, String columnName,
                              String condOp, String value )
    {
        _tables.put( tableName, tableName );
        _conds.addElement( tableName + JDBCSyntax.TableColumnSeparator + columnName +
                           condOp + value );
    }


    public void addInnerJoin( String leftTable, String leftColumn,
                              String rightTable, String rightColumn )
    {
        _tables.put( leftTable, leftTable );
        _tables.put( rightTable, rightTable );
        _joins.addElement( new Join( leftTable, leftColumn, rightTable, rightColumn, false ) );
    }


    public void addOuterJoin( String leftTable, String leftColumn,
                              String rightTable, String rightColumn )
    {
        _tables.put( leftTable, leftTable );
        _tables.put( rightTable, rightTable );
        _joins.addElement( new Join( leftTable, leftColumn, rightTable, rightColumn, true ) );
    }


    protected String getTableList()
    {
        Enumeration  enum;
        StringBuffer sql;

        sql = new StringBuffer();
        enum = _tables.elements();
        while ( enum.hasMoreElements() ) {
            sql.append( (String) enum.nextElement() );
            if ( enum.hasMoreElements() )
                sql.append( JDBCSyntax.TableSeparator );
        }
        return sql.toString();
    }


    protected String getColumnList()
    {
        StringBuffer sql;

        if ( _cols.size() == 0 )
            return "1";

        sql = new StringBuffer();
        for ( int i = 0 ; i < _cols.size() ; ++i ) {
            if ( i > 0 )
                sql.append( JDBCSyntax.ColumnSeparator );
            sql.append( (String) _cols.elementAt( i ) );
        }
        return sql.toString();
    }


    protected String getConditionList()
    {
        Enumeration  enum;
        StringBuffer sql;

        sql = new StringBuffer();
        enum = _conds.elements();
        while ( enum.hasMoreElements() ) {
            sql.append( (String) enum.nextElement() );
            if ( enum.hasMoreElements() )
                sql.append( JDBCSyntax.And );
        }
        return sql.toString();
    }


    public String getStatement( boolean lock )
    {
        StringBuffer sql;
        boolean      first;

        sql = new StringBuffer();
        sql.append( JDBCSyntax.Select );
        sql.append( getColumnList() );
        sql.append( JDBCSyntax.From );
        sql.append( getTableList() );

        first = true;
        if ( _conds.size() > 0 ) {
            if ( first ) {
                sql.append( JDBCSyntax.Where );
                first = false;
            } else
                sql.append( JDBCSyntax.And );
            sql.append( getConditionList() );
        }
        for ( int i = 0 ; i < _joins.size() ; ++i ) {
            if ( first ) {
                sql.append( JDBCSyntax.Where );
                first = false;
            } else
                sql.append( JDBCSyntax.And );
            sql.append( ( (Join) _joins.elementAt( i ) ).toString() );
        }

        if ( lock )
            sql.append( " FOR UPDATE" );
        return sql.toString();
    }


    public String toString()
    {
        return "<" + getStatement( false ) + ">";
    }


    public Object clone()
    {
        JDBCQueryExpression clone;

        clone = new JDBCQueryExpression();
        clone._tables = (Hashtable) _tables.clone();
        clone._conds = (Vector) _conds.clone();
        clone._cols = (Vector) _cols.clone();
        clone._joins = (Vector) _joins.clone();
        return clone;
    }


    static class Join
    {

        private final String   _leftTable;

        private final String[] _leftColumns;

        private final String   _rightTable;

        private final String[] _rightColumns;

        private final boolean  _outer;

        Join( String leftTable, String leftColumn, String rightTable, String rightColumn,
              boolean outer )
        {
            _leftTable = leftTable;
            _leftColumns = new String[] { leftColumn };
            _rightTable = rightTable;
            _rightColumns = new String[] { rightColumn };
            _outer = outer;
        }

        Join( String leftTable, String[] leftColumns, String rightTable, String[] rightColumns,
              boolean outer )
        {
            _leftTable = leftTable;
            _leftColumns = (String[]) leftColumns.clone();
            _rightTable = rightTable;
            _rightColumns =(String[]) rightColumns.clone();
            _outer = outer;
        }

        public String toString()
        {
            StringBuffer sql;

            sql = new StringBuffer();
            for ( int i = 0 ; i < _leftColumns.length ; ++i ) {
                if ( i > 0 )
                    sql.append( JDBCSyntax.And );
                sql.append( _leftTable + JDBCSyntax.TableColumnSeparator + _leftColumns[ i ] );
                sql.append( OpEquals );
                sql.append( _rightTable + JDBCSyntax.TableColumnSeparator + _rightColumns[ i ] );
            }
            return sql.toString();
        }

    }


}


