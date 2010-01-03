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

import org.castor.cpa.query.Literal;

/**
 * Junit Test for testing Boolean Literal class.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public final class TestBooleanLiteral extends TestCase {
    //--------------------------------------------------------------------------
    
    /**
     * Junit Test for instance Boolean Literal.
     */
    public void testInstance() {
        Literal n = new BooleanLiteral(true);
        assertTrue(n != null);
    }
    
    /**
     * Junit Test for constructor and getter.
     */
    public void testConstructor() {
        BooleanLiteral n = new BooleanLiteral(true);
        assertEquals(true, n.getValue()); 

        n = new BooleanLiteral(false);
        assertEquals(false, n.getValue()); 
    }

    /**
     * Junit Test for Boolean Literal toString method.
     */
    public void testToString() {
        BooleanLiteral n = new BooleanLiteral(true);
        assertEquals("true", n.toString()); 

        n = new BooleanLiteral(false);
        assertEquals("false", n.toString()); 
    }

    //--------------------------------------------------------------------------
}
