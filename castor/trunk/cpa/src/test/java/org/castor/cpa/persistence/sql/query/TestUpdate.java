/*
 * Copyright 2010 Ralf Joachim, Ahmad Hassan, Dennis Butterstein
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
 * Test if Update works as expected.
 *
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @author <a href="mailto:madsheepscarer AT googlemail DOT com">Dennis Butterstein</a>
 * @version $Revision$ $Date: 2009-07-13 17:22:43 (Mon, 13 Jul 2009) $
 */
public final class TestUpdate extends TestCase {
    public void testExtendsHierarchy() {
        assertTrue(QueryObject.class.isAssignableFrom(Update.class));
    }
    
    public void testConstructor() {
        Update update = null;
        try {
            update = new Update(null);
            fail("should throw NullPointerException");
        } catch (NullPointerException ex) {
            assertNull(update);
        } catch (Exception ex) {
            fail("should throw NullPointerException");
        }

        try {
            update = new Update("mytable");
            assertEquals("UPDATE mytable SET ", update.toString());
        } catch (Exception ex) {
            fail("should not throw exception");
        }
    }

    public void testAssignment() {
        Update update = new Update("mytable");
        assertEquals("UPDATE mytable SET ", update.toString());
        
        update.addAssignment(new Assignment(new Column("mycol1"), new Parameter("mycol1")));
        assertEquals("UPDATE mytable SET mycol1=?", update.toString());
        
        update.addAssignment(new Column("mycol2"), new Parameter("mycol2"));
        assertEquals("UPDATE mytable SET mycol1=?, mycol2=?", update.toString());
    }

    public void testCondition() {
        Update update = new Update("mytable");
        assertNull(update.getCondition());
        assertEquals("UPDATE mytable SET ", update.toString());
        
        Condition condition = new Column("mycol1").equal(new Parameter("mycol1"));
        update.setCondition(condition);
        assertEquals(condition, update.getCondition());
        assertEquals("UPDATE mytable SET  WHERE mycol1=?", update.toString());
        
        condition = condition.and(new Column("mycol2").equal(new Parameter("mycol2")));
        update.setCondition(condition);
        assertEquals(condition, update.getCondition());
        assertEquals("UPDATE mytable SET  WHERE mycol1=? AND mycol2=?", update.toString());

        condition = new AndCondition();
        update.setCondition(condition);
        assertEquals(condition, update.getCondition());
        assertEquals("UPDATE mytable SET  WHERE ", update.toString());
    }

    public void testAssignmentWithCondition() {
        Update update = new Update("mytable");
        update.addAssignment(new Column("mycol1"), new Parameter("mycol1"));
        update.setCondition(new Column("mycol2").equal(new Parameter("mycol2")));
        assertEquals("UPDATE mytable SET mycol1=? WHERE mycol2=?", update.toString());
    }

    public void testWithoutCondition() {
        Update update = new Update("mytable");
        QueryContext ctx = new QueryContext();
        update.toQueryString(ctx);
        assertEquals("UPDATE mytable SET ", ctx.toString());
    }

    public void testAssignmentWithoutCondition() {
        Update update = new Update("mytable");
        QueryContext ctx = new QueryContext();
        update.addAssignment(new Column("mycol1"), new Parameter("mycol1"));
        update.addAssignment(new Column("mycol2"), new Parameter("mycol2"));
        update.toQueryString(ctx);
        assertEquals("UPDATE mytable SET mycol1=?, mycol2=?", ctx.toString());
    }
}
