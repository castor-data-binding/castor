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
package org.castor.cpa.query.object;

import org.castor.cpa.query.Expression;
import org.castor.cpa.query.Field;
import org.castor.cpa.query.QueryObject;
import org.castor.cpa.query.object.expression.AbstractExpression;

import junit.framework.TestCase;

/**
 * Junit test for testing field implementation.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public final class TestFieldImpl extends TestCase {
    //--------------------------------------------------------------------------

    /**
     * Junit Test for instance.
     */
    public void testInstance() {
        QueryObject n = new FieldImpl(new MockField(), "field");
        assertTrue(n instanceof AbstractField);
        assertTrue(n instanceof AbstractExpression);
        assertTrue(n instanceof AbstractQueryObject);
        assertTrue(n instanceof Field);
        assertTrue(n instanceof Expression);
    }

    /**
     * Junit Test for constructor.
     */
    public void testConstructor() {
        Field field = new FieldImpl(new MockField(), "field");
        assertNotNull(field);

        try {
            new FieldImpl(null, "field");
            fail("NullPointerException expected");
        } catch (NullPointerException ex) {
            assertTrue(true);
        }

        try {
            new FieldImpl(new MockField(), null);
            fail("NullPointerException expected");
        } catch (NullPointerException ex) {
            assertTrue(true);
        }
    }
    
    /**
     * Junit Test for Getter and Setter methods.
     */
    public void testGSetter() {
        Field parent = new MockField();
        String fieldname = "field";
        
        FieldImpl field = new FieldImpl(parent, fieldname);
        assertEquals(parent, field.getParent());
        assertEquals(fieldname, field.getName());
    }
    
    /**
     * Junit Test for toString method.
     */
    public void testToString() {
        Field parent = new MockField();
        Field first = new FieldImpl(parent, "first");
        Field second = new FieldImpl(first, "second");
        assertEquals("field.first.second", second.toString()); 
    } 

    //--------------------------------------------------------------------------
}

