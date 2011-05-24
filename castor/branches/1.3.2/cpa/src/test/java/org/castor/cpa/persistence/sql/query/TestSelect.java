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
 * Test if Select works as expected.
 *
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2009-07-13 17:22:43 (Mon, 13 Jul 2009) $
 */
public final class TestSelect extends TestCase {
    public void testExtendsHierarchy() {
        assertTrue(QueryObject.class.isAssignableFrom(Select.class));
    }
    
    public void testConstructor() {
        Select select = null;
        try {
            select = new Select((String) null);
            fail("should throw NullPointerException");
        } catch (NullPointerException ex) {
            assertNull(select);
        } catch (Exception ex) {
            fail("should throw NullPointerException");
        }

        try {
            select = new Select((Qualifier) null);
            fail("should throw NullPointerException");
        } catch (NullPointerException ex) {
            assertNull(select);
        } catch (Exception ex) {
            fail("should throw NullPointerException");
        }

        try {
            select = new Select("mytable");
            assertEquals("SELECT * FROM mytable", select.toString());
        } catch (Exception ex) {
            fail("should not throw exception");
        }

        try {
            select = new Select(new Table("mytable"));
            assertEquals("SELECT * FROM mytable", select.toString());
        } catch (Exception ex) {
            fail("should not throw exception");
        }
    }

    public void testSelect() {
        Select select = new Select("mytable");
        assertEquals("SELECT * FROM mytable", select.toString());
        
        select.addSelect(new Column("mycol1"));
        assertEquals("SELECT mycol1 FROM mytable", select.toString());
        
        select.addSelect(new Column("mycol2"));
        assertEquals("SELECT mycol1, mycol2 FROM mytable", select.toString());
    }

    public void testCondition() {
        Select select = new Select("mytable");
        assertNull(select.getCondition());
        assertEquals("SELECT * FROM mytable", select.toString());
        
        Condition condition = new Column("mycol1").equal(new Parameter("mycol1"));
        select.setCondition(condition);
        assertEquals(condition, select.getCondition());
        assertEquals("SELECT * FROM mytable WHERE mycol1=?", select.toString());
        
        condition = condition.and(new Column("mycol2").equal(new Parameter("mycol2")));
        select.setCondition(condition);
        assertEquals(condition, select.getCondition());
        assertEquals("SELECT * FROM mytable WHERE mycol1=? AND mycol2=?", select.toString());

        condition = new AndCondition();
        select.setCondition(condition);
        assertEquals(condition, select.getCondition());
        assertEquals("SELECT * FROM mytable WHERE ", select.toString());
    }

    public void testSelectWithCondition() {
        Select select = new Select("mytable");
        select.addSelect(new Column("mycol1"));
        select.addSelect(new Column("mycol2"));
        select.setCondition(new Column("mycol3").equal(new Parameter("mycol3")));

        assertEquals("SELECT mycol1, mycol2 FROM mytable WHERE mycol3=?", select.toString());
    }
}
