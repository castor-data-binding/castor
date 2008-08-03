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

import junit.framework.TestCase;

import org.castor.cpa.query.Condition;
import org.castor.cpa.query.Field;
import org.castor.cpa.query.Foo;
import org.castor.cpa.query.Order;
import org.castor.cpa.query.OrderDirection;
import org.castor.cpa.query.QueryFactory;
import org.castor.cpa.query.Schema;
import org.castor.cpa.query.SelectQuery;

/**
 * Junit test for testing select query object.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public class TestSelect extends TestCase {
    //--------------------------------------------------------------------------
   
    public static void testWithField() {
        //--------------------------------------------------------------------------

        SelectQuery select = QueryFactory.newSelectQuery();
        select.setDistinct(true);
        Schema schema = select.newSchema(Foo.class, "o");
        select.addProjection(schema.field("bar"));
        select.addSchema(schema);
        Field field = schema.field("pen");
        Condition condition = field.equal(3).not().and(field.lessEqual(4));
        select.setWhere(condition);
        Order order = select.newOrder(schema.field("check"));
        select.setOrder(order);
        order.add(schema.field("cake"), OrderDirection.DESCENDING);
        select.setLimit(34, 45);
        String expected = "SELECT DISTINCT o.bar FROM org.castor.cpa.query.Foo AS o "
                        + "WHERE  NOT (o.pen = 3) AND (o.pen <= 4) "
                        + "ORDER BY o.check ASC, o.cake DESC LIMIT 34 OFFSET 45";
        assertEquals(expected, select.toString());
        System.out.println(select.toString());
    }
    //--------------------------------------------------------------------------
}
