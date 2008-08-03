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
package org.castor.cpa.query.object.condition;

import java.math.BigDecimal;

import junit.framework.TestCase;

import org.castor.cpa.query.Field;
import org.castor.cpa.query.Foo;
import org.castor.cpa.query.InCondition;
import org.castor.cpa.query.Literal;
import org.castor.cpa.query.QueryFactory;
import org.castor.cpa.query.Schema;
import org.castor.cpa.query.SelectQuery;
import org.castor.cpa.query.object.literal.LongLiteral;
import org.castor.cpa.query.object.parameter.NamedParameter;

/**
 * Junit Test for testing not in condition of query objects.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public class TestNotIn extends TestCase {
  //--------------------------------------------------------------
    
    private static String _common = "SELECT o.bar FROM org.castor.cpa.query.Foo AS o WHERE ";
    
  //--------------------------------------------------------------
    
    public static void testWithAll() {
        SelectQuery select = QueryFactory.newSelectQuery();
        Schema schema = select.newSchema(Foo.class, "o");
        select.addProjection(schema.field("bar"));
        Field field = schema.field("position");
        InCondition condition = field.notIn();
        //boolean
        condition.add(false);
        //long
        condition.add(3L);
        //double
        condition.add(4.0);
        //big decimal
        condition.add(new BigDecimal("99.56"));
        //String
        condition.add("String");
        //TODO Enum
        //Literal
        Literal l = new LongLiteral(55L);
        condition.add(l);
        //Parameter
        NamedParameter p = new NamedParameter("any");
        condition.add(p);
        select.setWhere(condition);
        select.addSchema(schema);
        String expected = "o.position NOT  IN (false, 3, 4.0, 99.56, 'String', 55, :any)";
        assertEquals(_common + expected, select.toString());
        System.out.println(select.toString());
    }
    
  //--------------------------------------------------------------
}
