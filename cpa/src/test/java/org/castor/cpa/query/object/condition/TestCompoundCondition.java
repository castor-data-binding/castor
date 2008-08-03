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

import junit.framework.TestCase;

import org.castor.cpa.query.Condition;
import org.castor.cpa.query.Field;
import org.castor.cpa.query.Foo;
import org.castor.cpa.query.QueryFactory;
import org.castor.cpa.query.Schema;
import org.castor.cpa.query.SelectQuery;

/**
 * Junit Test for testing compound condition of query objects.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public class TestCompoundCondition extends TestCase {
    //--------------------------------------------------------------
    
    private static String _common = "SELECT o.bar FROM org.castor.cpa.query.Foo AS o WHERE ";
    
    //--------------------------------------------------------------
    
    public static void testAnd() {
        SelectQuery select = QueryFactory.newSelectQuery();
        Schema schema = select.newSchema(Foo.class, "o");
        select.addProjection(schema.field("bar"));
        Field field = schema.field("position");
        Condition condition = field.notEqual(true).and(field.equal(4));
        select.setWhere(condition);
        select.addSchema(schema);
        String expected = "(o.position != true) AND ((o.position = 4))";
        assertEquals(_common + expected, select.toString());
        System.out.println(select.toString());
    }
    
    public static void testOr() {
        SelectQuery select = QueryFactory.newSelectQuery();
        Schema schema = select.newSchema(Foo.class, "o");
        select.addProjection(schema.field("bar"));
        Field field = schema.field("position");
        Condition condition = field.notEqual(true).or(field.equal(4));
        select.setWhere(condition);
        select.addSchema(schema);
        String expected = "(o.position != true) OR ((o.position = 4))";
        assertEquals(_common + expected, select.toString());
        System.out.println(select.toString());
    }
    
    public static void testOrAnd() {
        SelectQuery select = QueryFactory.newSelectQuery();
        Schema schema = select.newSchema(Foo.class, "o");
        select.addProjection(schema.field("bar"));
        Field field = schema.field("position");
        Condition condition = field.notEqual(true).or(field.equal(4).and(field.lessEqual(9)));
        select.setWhere(condition);
        select.addSchema(schema);
        String expected = "(o.position != true) OR ((o.position = 4) AND ((o.position <= 9)))";
        assertEquals(_common + expected, select.toString());
        System.out.println(select.toString());
    }
    
    public static void testAndOrNot() {
        SelectQuery select = QueryFactory.newSelectQuery();
        Schema schema = select.newSchema(Foo.class, "o");
        select.addProjection(schema.field("bar"));
        Field field = schema.field("position");
        Condition condition = field.notEqual(true).and(field.equal(4).or(field.lessEqual(9).not()));
        select.setWhere(condition);
        select.addSchema(schema);
        String expected =
            "(o.position != true) AND ((o.position = 4) OR ( NOT ((o.position <= 9))))";
        assertEquals(_common + expected, select.toString());
        System.out.println(select.toString());
    }
    
  //--------------------------------------------------------------
}
