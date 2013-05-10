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

import org.castor.cpa.persistence.sql.query.condition.AndCondition;
import org.castor.cpa.persistence.sql.query.condition.Condition;
import org.castor.cpa.persistence.sql.query.expression.Column;
import org.castor.cpa.persistence.sql.query.expression.Parameter;

import junit.framework.TestCase;

/**
 * Test if Delete works as expected.
 *
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2009-07-13 17:22:43 (Mon, 13 Jul 2009) $
 */
public final class TestDelete extends TestCase {
    public void testExtendsHierarchy() {
        assertTrue(QueryObject.class.isAssignableFrom(Delete.class));
    }
    
    public void testConstructor() {
        Delete delete = null;
        try {
            delete = new Delete(null);
            fail("should throw NullPointerException");
        } catch (NullPointerException ex) {
            assertNull(delete);
        } catch (Exception ex) {
            fail("should throw NullPointerException");
        }

        try {
            delete = new Delete("mytable");
            assertEquals("DELETE FROM mytable", delete.toString());
        } catch (Exception ex) {
            fail("should not throw exception");
        }
    }

    public void testCondition() {
        Delete delete = new Delete("mytable");
        assertNull(delete.getCondition());
        assertEquals("DELETE FROM mytable", delete.toString());
        
        Condition condition = new Column("mycol1").equal(new Parameter("mycol1"));
        delete.setCondition(condition);
        assertEquals(condition, delete.getCondition());
        assertEquals("DELETE FROM mytable WHERE mycol1=?", delete.toString());
        
        condition = condition.and(new Column("mycol2").equal(new Parameter("mycol2")));
        delete.setCondition(condition);
        assertEquals(condition, delete.getCondition());
        assertEquals("DELETE FROM mytable WHERE mycol1=? AND mycol2=?", delete.toString());

        condition = new AndCondition();
        delete.setCondition(condition);
        assertEquals(condition, delete.getCondition());
        assertEquals("DELETE FROM mytable WHERE ", delete.toString());
    }
}
