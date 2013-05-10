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
import org.castor.cpa.query.Foo;
import org.castor.cpa.query.QueryObject;
import org.castor.cpa.query.Schema;
import org.castor.cpa.query.object.expression.AbstractExpression;

import junit.framework.TestCase;

/**
 * Junit test for testing schema implementation.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public final class TestSchemaImpl extends TestCase {
    //--------------------------------------------------------------------------

    /**
     * Junit Test for instance.
     */
    public void testInstance() {
        QueryObject n = new SchemaImpl("schema", "identifier");
        assertTrue(n instanceof AbstractField);
        assertTrue(n instanceof AbstractExpression);
        assertTrue(n instanceof AbstractQueryObject);
        assertTrue(n instanceof Schema);
        assertTrue(n instanceof Field);
        assertTrue(n instanceof Expression);
    }

    /**
     * Junit Test for constructor.
     */
    public void testConstructor() {
        Schema schema1 = new SchemaImpl("schema", "identifier");
        assertNotNull(schema1);

        Schema schema2 = new SchemaImpl(Foo.class, "identifier");
        assertNotNull(schema2);

        try {
            new SchemaImpl((String) null, "identifier");
            fail("NullPointerException expected");
        } catch (NullPointerException ex) {
            assertTrue(true);
        }

        try {
            new SchemaImpl("schema", null);
            fail("NullPointerException expected");
        } catch (NullPointerException ex) {
            assertTrue(true);
        }

        try {
            new SchemaImpl((Class) null, "identifier");
            fail("NullPointerException expected");
        } catch (NullPointerException ex) {
            assertTrue(true);
        }

        try {
            new SchemaImpl(Foo.class, null);
            fail("NullPointerException expected");
        } catch (NullPointerException ex) {
            assertTrue(true);
        }
    }
    
    /**
     * Junit Test for Getter and Setter methods.
     */
    public void testGSetter() {
        String schemaname = "schema";
        Class type = Foo.class;
        String identifier = "identifier";
        
        SchemaImpl schema1 = new SchemaImpl(schemaname, identifier);
        assertEquals(schemaname, schema1.getAbstractName());
        assertNull(schema1.getTypeName());
        assertNull(schema1.getType());
        assertEquals(identifier, schema1.getIdentifier());
        
        SchemaImpl schema2 = new SchemaImpl(type, identifier);
        assertNull(schema2.getAbstractName());
        assertEquals(Foo.class.getName(), schema2.getTypeName());
        assertEquals(Foo.class, schema2.getType());
        assertEquals(identifier, schema2.getIdentifier());
    }
    
    /**
     * Junit Test for toString method.
     */
    public void testToString() {
        String schemaname = "schema";
        Class type = Foo.class;
        String identifier = "identifier";
        
        SchemaImpl schema1 = new SchemaImpl(schemaname, identifier);
        assertEquals("identifier", schema1.toString(new StringBuilder()).toString());
        assertEquals("schema AS identifier", schema1.toFullString(new StringBuilder()).toString());
        assertEquals("schema AS identifier", schema1.toFullString());
        
        SchemaImpl schema2 = new SchemaImpl(type, identifier);
        String expected = Foo.class.getName() + " AS identifier";
        assertEquals("identifier", schema2.toString(new StringBuilder()).toString());
        assertEquals(expected, schema2.toFullString(new StringBuilder()).toString());
        assertEquals(expected, schema2.toFullString());
    } 

    //--------------------------------------------------------------------------
}

