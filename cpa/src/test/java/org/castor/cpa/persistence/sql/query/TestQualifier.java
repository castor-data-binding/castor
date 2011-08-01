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

import junit.framework.TestCase;

import org.castor.cpa.persistence.sql.query.expression.Column;

/** 
 * Test if Qualifier works as expected.
 *
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date$
 */
public final class TestQualifier extends TestCase {
    public void testExtendsHierarchy() {
        assertTrue(QueryObject.class.isAssignableFrom(Qualifier.class));
    }
    
    public void testConstructor() {
        Qualifier qualifier = null;
        try {
            qualifier = new QualifierMock(null);
            fail("should throw NullPointerException");
        } catch (NullPointerException ex) {
            assertNull(qualifier);
        } catch (Exception ex) {
            fail("should throw NullPointerException");
        }

        try {
            qualifier = new QualifierMock("myqualifier");
            assertEquals("myqualifier", qualifier.name());
            assertEquals("myqualifier", qualifier.toString());
        } catch (Exception ex) {
            fail("should not throw exception");
        }
    }
    
    public void testColumnFactory() {
        Qualifier qualifier = new QualifierMock("myqualifier");
        Column column = qualifier.column("mycolumn");
        assertEquals(qualifier, column.qualifier());
    }
}
