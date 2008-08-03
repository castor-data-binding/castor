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
package org.castor.cpa.query.object.expression;

import java.math.BigDecimal;

import junit.framework.TestCase;

import org.castor.cpa.query.Condition;
import org.castor.cpa.query.Expression;
import org.castor.cpa.query.Field;
import org.castor.cpa.query.Foo;
import org.castor.cpa.query.QueryFactory;
import org.castor.cpa.query.Schema;
import org.castor.cpa.query.SelectQuery;

/**
 * Junit Test for testing remainder arithmetic expression of query objects.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public class TestRemainder extends TestCase {
    //--------------------------------------------------------------------------

    private static String _common = "SELECT o.bar FROM org.castor.cpa.query.Foo AS o WHERE ";

    //--------------------------------------------------------------------------

    public static void testAll() {
        SelectQuery select = QueryFactory.newSelectQuery();
        Schema schema = select.newSchema(Foo.class, "o");
        select.addProjection(schema.field("bar"));
        Field field = schema.field("position");
        Expression exp = new MockExpression();
        Condition condition = field
        .remainder(2)
        .remainder(4.0)
        .remainder(new BigDecimal("4"))
        .remainder(exp)
        .equal(3);
        select.setWhere(condition);
        select.addSchema(schema);
        String expected = "o.position % 2 % 4.0 % 4 % expression = 3";
        assertEquals(_common + expected, select.toString());
        System.out.println(select.toString());
    }

    //--------------------------------------------------------------------------
}
