/*
 * Copyright 2009 Ralf Joachim, Ahmad Hassan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.castor.cpa.persistence.sql.query;

/**
 * Constants for SQL queries.
 * 
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public final class QueryConstants {
    //-----------------------------------------------------------------------------------    

    /** 'SELECT' keyword of a SQL delete query. */
    public static final String SELECT = "SELECT";
    
    /** 'DELETE' keyword of a SQL delete query. */
    public static final String DELETE = "DELETE";
    
    /** 'UPDATE' keyword of a SQL update query. */
    public static final String UPDATE = "UPDATE";
    
    /** 'SET' keyword of a SQL update query. */
    public static final String SET = "SET";   
    
    /** 'FROM' keyword of a SQL query. */
    public static final String FROM = "FROM";

    /** 'WHERE' keyword of a SQL query. */
    public static final String WHERE = "WHERE";
    
    /** 'AND' keyword for conditions of SQL queries. */
    public static final String AND = "AND";

    /** 'OR' keyword for conditions of SQL queries. */
    public static final String OR = "OR";

    /** 'NOT' keyword for conditions of SQL queries. */
    public static final String NOT = "NOT";

    /** 'IS' keyword for conditions of SQL queries. */
    public static final String IS = "IS";

    /** 'NULL' keyword for conditions of SQL queries. */
    public static final String NULL = "NULL";

    /** Character to use for spaces in SQL query. */
    public static final char SPACE = ' ';
    
    /** Character separating qualifier and column in a SQL query. */
    public static final char DOT = '.';
    
    /** Character representing a parameter in a SQL query. */
    public static final char PARAMETER = '?';
    
    /** Left parenthesis character for SQL query. */
    public static final char LPAREN = '(';
    
    /** Right parenthesis character for SQL query. */
    public static final char RPAREN = ')';
    
    /** Character to assign a value to a column in set clause of an SQL update query. */
    public static final char ASSIGN = '=';
    
    /** Character to separate multiple assignments in set clause of an SQL update query.  */
    public static final char SEPERATOR = ',';

    //-----------------------------------------------------------------------------------
    
    /**
     * Hide utility class constructor.
     */
    private QueryConstants() { }
    
    //-----------------------------------------------------------------------------------    
}
