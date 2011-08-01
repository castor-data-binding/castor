/*
 * Copyright 2010 Dennis Butterstein, Ralf Joachim
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
 *
 * $Id$
 */

package org.castor.cpa.persistence.sql.query.visitor;

import static org.junit.Assert.assertEquals;

import org.castor.cpa.persistence.sql.query.Select;
import org.castor.cpa.persistence.sql.query.Visitor;
import org.junit.Test;

/**
 * Test if MySQLQueryVisitor works as expected.
 *
 * @author <a href="mailto:madsheepscarer AT googlemail DOT com">Dennis Butterstein</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date$
 */
public final class TestMySQLQueryVisitor extends TestDefaultQueryVisitor {
    //---------------------------SELECT--------------------------------------------------------

    @Test
    public void testSelectNoConditionNoExpressionWithLock() throws Exception {
        Select select = new Select("TestTable");
        select.setLocked(true);

        Visitor queryVis = getVisitor();
        queryVis.visit(select);
        
        String expected = "SELECT * FROM TestTable FOR UPDATE";

        assertEquals(expected, queryVis.toString());
    }

    @Test
    public void testHandleLock() throws Exception {
        Select select = new Select("Test");
        select.setLocked(true);

        Visitor queryVis = getVisitor();
        ((MySQLQueryVisitor) queryVis).handleLock(select);

        assertEquals(" FOR UPDATE", queryVis.toString());
    }

    protected Visitor getVisitor() {
        return new MySQLQueryVisitor();
    }

    //-----------------------------------------------------------------------------------
}
