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
package org.castor.cpa.persistence.sql.query.expression;

import junit.framework.TestCase;

/** 
 * Test if NextVal works as expected.
 *
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2009-07-13 17:22:43 (Mon, 13 Jul 2009) $
 */
public final class TestNextVal extends TestCase {
    public void testExtendsHierarchy() {
        assertTrue(Function.class.isAssignableFrom(NextVal.class));
    }
    
    public void testConstructor() {
        NextVal val = null;
        try {
            val = new NextVal(null);
            fail("should throw NullPointerException");
        } catch (NullPointerException ex) {
            assertNull(val);
        } catch (Exception ex) {
            fail("should throw NullPointerException");
        }

        try {
            val = new NextVal("mysequence");
            assertNotNull(val);
        } catch (Exception ex) {
            fail("should not throw exception");
        }
    }
    
    public void testUsage() {
        NextVal val = new NextVal("mysequence");
        assertEquals("mysequence.nextval", val.toString());
    }
}
