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
package org.castor.cpa.query;

import junit.framework.TestCase;

/**
 * Junit test for query object Condition implimentation.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr
 *          2006) $
 * @since 1.3
 */
public final class TestQOCondition extends TestCase {
    //--------------------------------------------------------------------------

    public static void testEqual() {
        SelectQuery select = QueryFactory.newSelectQuery();
        Schema schema = select.newSchema(Foo.class, "o");
        select.addProjection(schema.field("bar"));
        Field field = schema.field("position");
        Condition condition = field.equal(2);
        select.setWhere(condition);
        select.addSchema(schema);
        // System.out.println(select.toString());
        String expected = "SELECT o.bar FROM org.castor.cpa.query.Foo AS o WHERE (o.position = 2)";
        String actual = select.toString();
        assertEquals(actual, expected);
    }

    public static void testNotEqual() {
        SelectQuery select = QueryFactory.newSelectQuery();
        Schema schema = select.newSchema(Foo.class, "o");
        select.addProjection(schema.field("bar"));
        Field field = schema.field("position");
        Condition condition = field.notEqual(true);
        select.setWhere(condition);
        select.addSchema(schema);
        // System.out.println(select.toString());
        String expected = "SELECT o.bar FROM org.castor.cpa.query.Foo AS o "
                + "WHERE (o.position <> true)";
        String actual = select.toString();
        assertEquals(actual, expected);
    }

    public static void testLessThan() {
        SelectQuery select = QueryFactory.newSelectQuery();
        Schema schema = select.newSchema(Foo.class, "o");
        select.addProjection(schema.field("bar"));
        Field field = schema.field("position");
        Condition condition = field.lessThan(7.0);
        select.setWhere(condition);
        select.addSchema(schema);
        // System.out.println(select.toString());
        String expected = "SELECT o.bar FROM org.castor.cpa.query.Foo AS o"
                + " WHERE (o.position < 7.0)";
        String actual = select.toString();
        assertEquals(actual, expected);
    }

    public static void testLessEqual() {
        SelectQuery select = QueryFactory.newSelectQuery();
        Schema schema = select.newSchema(Foo.class, "o");
        select.addProjection(schema.field("bar"));
        Field field = schema.field("position");
        Condition condition = field.lessEqual("try");
        select.setWhere(condition);
        select.addSchema(schema);
        // System.out.println(select.toString());
        String expected = "SELECT o.bar FROM org.castor.cpa.query.Foo AS o"
                + " WHERE (o.position <= 'try')";
        String actual = select.toString();
        assertEquals(actual, expected);
    }

    public static void testGreaterEqual() {
        SelectQuery select = QueryFactory.newSelectQuery();
        Schema schema = select.newSchema(Foo.class, "o");
        select.addProjection(schema.field("bar"));
        Field field = schema.field("position");
        Condition condition = field.greaterEqual(40);
        select.setWhere(condition);
        select.addSchema(schema);
        // System.out.println(select.toString());
        String expected = "SELECT o.bar FROM org.castor.cpa.query.Foo AS o"
                + " WHERE (o.position >= 40)";
        String actual = select.toString();
        assertEquals(actual, expected);
    }

    public static void testGreaterThan() {
        SelectQuery select = QueryFactory.newSelectQuery();
        Schema schema = select.newSchema(Foo.class, "o");
        select.addProjection(schema.field("bar"));
        Field field = schema.field("position");
        Condition condition = field.greaterThan(21.9);
        select.setWhere(condition);
        select.addSchema(schema);
        // System.out.println(select.toString());
        String expected = "SELECT o.bar FROM org.castor.cpa.query.Foo AS o "
                + "WHERE (o.position > 21.9)";
        String actual = select.toString();
        assertEquals(actual, expected);
    }

    public static void testLike() {
        SelectQuery select = QueryFactory.newSelectQuery();
        Schema schema = select.newSchema(Foo.class, "o");
        select.addProjection(schema.field("bar"));
        Field field = schema.field("position");
        Condition condition = field.like("A%");
        select.setWhere(condition);
        select.addSchema(schema);
        // System.out.println(select.toString());
        String expected = "SELECT o.bar FROM org.castor.cpa.query.Foo AS o"
                + " WHERE (o.position LIKE 'A%')";
        String actual = select.toString();
        assertEquals(actual, expected);
    }

    public static void testNotLike() {
        SelectQuery select = QueryFactory.newSelectQuery();
        Schema schema = select.newSchema(Foo.class, "o");
        select.addProjection(schema.field("bar"));
        Field field = schema.field("position");
        Condition condition = field.notLike("A%");
        select.setWhere(condition);
        select.addSchema(schema);
        // System.out.println(select.toString());
        String expected = "SELECT o.bar FROM org.castor.cpa.query.Foo AS o"
                + " WHERE (o.position NOT LIKE 'A%')";
        String actual = select.toString();
        assertEquals(actual, expected);
    }

    public static void testBetween() {
        SelectQuery select = QueryFactory.newSelectQuery();
        Schema schema = select.newSchema(Foo.class, "o");
        select.addProjection(schema.field("bar"));
        Field field = schema.field("position");
        Condition condition = field.between("low", "high");
        select.setWhere(condition);
        select.addSchema(schema);
        // System.out.println(select.toString());
        String expected = "SELECT o.bar FROM org.castor.cpa.query.Foo AS o"
                + " WHERE (o.position BETWEEN 'low' AND 'high')";
        String actual = select.toString();
        assertEquals(actual, expected);
    }

    public static void testNotBetween() {
        SelectQuery select = QueryFactory.newSelectQuery();
        Schema schema = select.newSchema(Foo.class, "o");
        select.addProjection(schema.field("bar"));
        Field field = schema.field("position");
        Condition condition = field.notBetween("low", "high");
        select.setWhere(condition);
        select.addSchema(schema);
        // System.out.println(select.toString());
        String expected = "SELECT o.bar FROM org.castor.cpa.query.Foo AS o"
                + " WHERE (o.position NOT BETWEEN 'low' AND 'high')";
        String actual = select.toString();
        assertEquals(actual, expected);
    }

    //--------------------------------------------------------------------------
}
