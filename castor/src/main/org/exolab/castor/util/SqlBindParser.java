/*
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *	  statements and notices.  Redistributions must also contain a
 *	  copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *	  above copyright notice, this list of conditions and the
 *	  following disclaimer in the documentation and/or other
 *	  materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *	  products derived from this Software without prior written
 *	  permission of Intalio, Inc.  For written permission,
 *	  please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *	  nor may "Exolab" appear in their names without prior written
 *	  permission of Intalio, Inc. Exolab is a registered
 *	  trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *	  (http://www.exolab.org/).
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
 * Copyright 2004 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.util;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.exolab.castor.jdo.engine.JDBCSyntax;

/**
 * Utility class to parse an SQL or OQL expression for bind variables
 * Bind variables are subexpressions of the form "$n", where n is a
 * positive integer number. 
 * 
 * To parse the expression, call SqlBindParser.next() in a loop
 * until no more bind variable can be found. Each call moves on to
 * the next bind variable and returns true if another could be found.
 *  
 * Inside the loop call SqlBindParser.getBindExpr() to access the
 * current bind variable expression. ("$1", "$2", ...)
 * 
 * SqlBindParser.getParamNumber() can be used to read the parameter
 * number (1, 2, ...).
 * 
 * If you are interested in the remainder of the expression string,
 * just call getLastExpr() to get the last processed substring.
 *
 * For example, when parsing the expression
 * "select * from x where id between $1 and $2"
 * this gives you the following function returns:
 * next()			-> true
 * getLastExpr()	-> "select * from x where id between "
 * getBindExpr()	-> "$1"
 * getParamNumber()	-> 1
 * next()			-> true
 * getLastExpr()	-> " and "
 * getBindExpr()	-> "$2"
 * getParamNumber()	-> 2
 * next()			-> false
 * getLastExpr()	-> ""
 *
 * @author Martin Fuchs <martin-fuchs AT gmx DOT net>
 */
final public class SqlBindParser
{
	/** 
	 * complete SQL expression to be parsed 
	 */
    private String _sql;	
    
    /** 
     * current parse position 
     */
    private int _pos;
    
    /** 
     * last parse position 
     */
    private int _lastPos;	
    
    /** 
     * position of the current bind variable 
     */
    private int _bindPos;	

    /**
     * Create a new SqlBindParser instance to parse the expression in 'sql'.
     *
     * @param sql expression to be parsed
     */
    public SqlBindParser(String sql)
    {
        _sql = sql;
        _pos = 0;
        _lastPos = 0;
        _bindPos = 0;
    }

    /**
     * Move on to the next bind variable in '_sql'.
     *
     * @return true, if an bind variable could be found 
     */
    public boolean next()
    {
        _lastPos = _pos;

         // search for the begin of the next bind variable 
        int idxStart = _sql.indexOf('?', _pos);
        if (idxStart == -1) {
            _bindPos = _pos = _sql.length();
            return false;
        }

        _bindPos = idxStart;

         // search for the end of the bind variable
        int idxEnd = idxStart;
        do
            ++idxEnd;
        while(idxEnd<_sql.length() && Character.isDigit(_sql.charAt(idxEnd)));

        _pos = idxEnd;

        return true;
    }

    /**
     * Returns the expression substring beginning after the
     * last processed bind variable and ending just before the
     * current bind variable.
     *
     * @return last expression substring
     */
    public String getLastExpr()
    {
        return _sql.substring(_lastPos, _bindPos);
    }

    /**
     * Returns the current bind variable expression, e.g. "$1".
     *
     * @return current bind variable expression
     */
    public String getBindExpr()
    {
        return _sql.substring(_bindPos, _pos);
    }

    /**
     * Returns the parameter number of the current bind variable,
     * for example 1 a "$1" bind variable.
     * If an un-numbered bind variable "$" is found, 0 is returned.  
     * 
     * @return parameter number of current bind variable
     */
    public int getParamNumber()
    {
        int idx = _bindPos + 1;

        if (idx < _pos)
            return Integer.parseInt(_sql.substring(idx, _pos));
        else
            return 0;	// no numbered bind variable
    }


    /**
     * Creates a SQL statement from pre_sql, replacing bind expressions like "?1" by "?"
     * @param pre_sql SQL statement string with bind variables of the form "?1"
     * @return SQL statement string with bind variables of the form "?"
     */
    public static String getJdbcSql(String pre_sql)
    {
        StringBuffer sb = new StringBuffer();
        SqlBindParser parser = new SqlBindParser(pre_sql);

        while(parser.next()) {
            sb.append(parser.getLastExpr());
            sb.append(JDBCSyntax.Parameter);
        }

        sb.append(parser.getLastExpr());

        return sb.toString();
    }

    /**
     * Binds values to prepared SQL statement using the given
     * sql string as reference for the bind variable order.
     * @param pre_sql SQL statement string with bind variables of the form "?1"
     * @param sql
     * @param values
     * @throws SQLException
     */
    public static void bindJdbcValues(PreparedStatement stmt, String pre_sql, Object[] values) throws SQLException
    {
        SqlBindParser parser = new SqlBindParser(pre_sql);

        for(int i=1; parser.next(); ++i) {
            int bindNum = parser.getParamNumber();

            if (bindNum == 0)
                bindNum = i;	// handle CALL SQL statements with unnumbered bind variables

            stmt.setObject(i, values[bindNum-1]);
        }
    }
}