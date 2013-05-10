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
package org.castor.cpa.persistence.sql.query.condition;

/**
 * Enumeration of comparison operators.
 *  
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public enum CompareOperator {
    //-----------------------------------------------------------------------------------    

    /** Equal comparison operator. */
    EQ("=", "NE"),

    /** Not equal comparison operator. */
    NE("<>", "EQ"),

    /** Less than comparison operator. */
    LT("<", "GE"),

    /** Greater than or equal comparison operator. */
    GE(">=", "LT"),

    /** Greater than comparison operator. */
    GT(">", "LE"),

    /** Less than or equal comparison operator. */
    LE("<=", "GT");
    
    //-----------------------------------------------------------------------------------    

    /** String representation in SQL syntax. */
    private String _operator;
    
    /** Name of inverse comparison operator. */
    private String _inverse;
    
    //-----------------------------------------------------------------------------------    

    /**
     * Construct a comparison operator with given SQL string representation and inverse operator.
     * 
     * @param operator String representation in SQL syntax.
     * @param inverse Name of inverse comparison operator.
     */
    CompareOperator(final String operator, final String inverse) {
        _operator = operator;
        _inverse = inverse;
    }
    
    //-----------------------------------------------------------------------------------    

    /**
     * Returns the inverse comparison operator.
     * 
     * @return Inverse comparison operator.
     */
    public CompareOperator inverse() {
        return valueOf(_inverse);
    }
    
    //-----------------------------------------------------------------------------------    

    /**
     * Returns a string representation of the comparison operator in SQL syntax.
     * 
     * @return String representation of the comparison operator in SQL syntax.
     */
    public String toString() {
        return _operator;
    }

    //-----------------------------------------------------------------------------------    
}
