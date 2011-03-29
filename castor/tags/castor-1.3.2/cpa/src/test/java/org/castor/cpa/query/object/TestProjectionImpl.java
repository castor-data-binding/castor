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
import org.castor.cpa.query.Projection;
import org.castor.cpa.query.QueryObject;
import org.castor.cpa.query.object.expression.AbstractExpression;

import junit.framework.TestCase;

/**
 * Junit test for testing projection implementation.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public final class TestProjectionImpl extends TestCase {
    //--------------------------------------------------------------------------

    /**
     * Junit Test for instance.
     */
    public void testInstance() {
        QueryObject n = new ProjectionImpl(new MockField());
        assertTrue(n instanceof AbstractField);
        assertTrue(n instanceof AbstractExpression);
        assertTrue(n instanceof AbstractQueryObject);
        assertTrue(n instanceof Projection);
        assertTrue(n instanceof Field);
        assertTrue(n instanceof Expression);
    }

    /**
     * Junit Test for constructor.
     */
    public void testConstructor() {
        Projection projection1 = new ProjectionImpl(new MockField());
        assertNotNull(projection1);

        Projection projection2 = new ProjectionImpl(new MockField(), "alias");
        assertNotNull(projection2);

        Projection projection3 = new ProjectionImpl(new MockField(), null);
        assertNotNull(projection3);

        try {
            new ProjectionImpl(null);
            fail("NullPointerException expected");
        } catch (NullPointerException ex) {
            assertTrue(true);
        }

        try {
            new ProjectionImpl(null, "alias");
            fail("NullPointerException expected");
        } catch (NullPointerException ex) {
            assertTrue(true);
        }
    }
    
    /**
     * Junit Test for Getter and Setter methods.
     */
    public void testGSetter() {
        Field field = new MockField();
        String alias = "alias";
        
        ProjectionImpl projection1 = new ProjectionImpl(field);
        assertEquals(field, projection1.getField());
        assertNull(projection1.getAlias());
        
        ProjectionImpl projection2 = new ProjectionImpl(field, null);
        assertEquals(field, projection2.getField());
        assertNull(projection2.getAlias());
        
        ProjectionImpl projection3 = new ProjectionImpl(field, alias);
        assertEquals(field, projection3.getField());
        assertEquals(alias, projection3.getAlias());
    }
    
    /**
     * Junit Test for toString method.
     */
    public void testToString() {
        Field field = new MockField();
        String alias = "alias";

        ProjectionImpl projection = new ProjectionImpl(field, alias);
        assertEquals("alias", projection.toString(new StringBuilder()).toString());
        assertEquals("field AS alias", projection.toFullString(new StringBuilder()).toString());
        assertEquals("field AS alias", projection.toFullString());
    } 

    //--------------------------------------------------------------------------
}

