/*
 * Copyright 2008 Udai Gupta, Ralf Joachim
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
package org.castor.cpa.query.object.literal;

import junit.framework.TestCase;

import org.castor.cpa.query.Expression;
import org.castor.cpa.query.Literal;
import org.castor.cpa.query.QueryObject;
import org.castor.cpa.query.object.expression.AbstractExpression;

/**
 * Junit Test for testing String Literal class.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public final class TestStringLiteral extends TestCase {
    //--------------------------------------------------------------------------
    
    /**
     * Junit Test for instance String Literal.
     */
    public void testInstance() {
        QueryObject n = new StringLiteral("");
        assertTrue(n instanceof AbstractLiteral);
        assertTrue(n instanceof Literal);
        assertTrue(n instanceof AbstractExpression);
        assertTrue(n instanceof Expression);
    }
    
    /**
     * Junit Test for constructor and getter.
     */
    public void testConstructor() {
        try {
            new StringLiteral(null);
            fail("expected NullPointerException");
        } catch (NullPointerException ex) {
            assertTrue(true);
        }
        
        StringLiteral n = new StringLiteral("a string");
        assertEquals("a string", n.getValue()); 
    }
    
    /**
     * Junit Test for String Literal toString method.
     */
    public void testToString() {
        StringLiteral n = new StringLiteral("a string");
        assertEquals("'a string'", n.toString());
        
        n = new StringLiteral("a string with quote's");
        assertEquals("'a string with quote''s'", n.toString()); 
    } 

    //--------------------------------------------------------------------------
}
