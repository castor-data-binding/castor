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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.castor.cpa.persistence.sql.query.Select;
import org.castor.cpa.persistence.sql.query.Table;
import org.castor.cpa.persistence.sql.query.expression.Column;
import org.junit.Test;

/**
 * Test if ParameterVisitor works as expected.
 *
 * @author <a href="mailto:madsheepscarer AT googlemail DOT com">Dennis Butterstein</a>
 * @version $Revision: 8469 $ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public final class TestUncoupleVisitor  {
    //---------------------------SELECT--------------------------------------------------------

    @Test
    public void testVisitSelect() {
        Table table = new Table("TestTable");
        Select select = new Select(table);
        Column column = new Column(table, "TestColumn");
        Column column2 = new Column(table, "TestColumn2");
        select.addSelect(column);
        select.addSelect(column2);

        UncoupleVisitor uncle = new UncoupleVisitor();
        uncle.visit(select);

        Map<String, Integer> resultColumnMap = uncle.getResultColumnMap();

        assertEquals(2, resultColumnMap.size());
        assertTrue(resultColumnMap.containsKey("TestTable.TestColumn"));
        assertEquals(new Integer(1), resultColumnMap.get("TestTable.TestColumn"));
        assertTrue(resultColumnMap.containsKey("TestTable.TestColumn2"));
        assertEquals(new Integer(2), resultColumnMap.get("TestTable.TestColumn2"));
    }

    @Test
    public void testVisitColumn() {
        Table table = new Table("TestTable");
        Column column = new Column(table, "TestColumn");
        Column column2 = new Column(table, "TestColumn2");

        UncoupleVisitor uncle = new UncoupleVisitor();
        uncle.visit(column);

        Map<String, Integer> resultColumnMap = uncle.getResultColumnMap();

        assertEquals(1, resultColumnMap.size());
        assertTrue(resultColumnMap.containsKey("TestTable.TestColumn"));
        assertEquals(new Integer(1), resultColumnMap.get("TestTable.TestColumn"));

        uncle.visit(column2);

        assertEquals(2, resultColumnMap.size());
        assertTrue(resultColumnMap.containsKey("TestTable.TestColumn"));
        assertEquals(new Integer(1), resultColumnMap.get("TestTable.TestColumn"));
        assertTrue(resultColumnMap.containsKey("TestTable.TestColumn2"));
        assertEquals(new Integer(2), resultColumnMap.get("TestTable.TestColumn2"));
    }

    //-----------------------------------------------------------------------------------
}
