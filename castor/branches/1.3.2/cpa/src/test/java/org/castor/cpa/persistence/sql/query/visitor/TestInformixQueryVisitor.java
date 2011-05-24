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
 * $Id: SQLStatementDelete.java 8469 2009-12-28 16:47:54Z rjoachim $
 */

package org.castor.cpa.persistence.sql.query.visitor;

import org.castor.cpa.persistence.sql.query.Select;
import org.castor.cpa.persistence.sql.query.Visitor;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test if InformixQueryVisitor works as expected.
 *
 * @author <a href="mailto:madsheepscarer AT googlemail DOT com">Dennis Butterstein</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 8469 $ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public class TestInformixQueryVisitor extends TestDefaultQueryVisitor {
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
        ((InformixQueryVisitor) queryVis).handleLock(select);

        assertEquals(" FOR UPDATE", queryVis.toString());
    }

    protected Visitor getVisitor() {
        return new InformixQueryVisitor();
    }

    //-----------------------------------------------------------------------------------
}
