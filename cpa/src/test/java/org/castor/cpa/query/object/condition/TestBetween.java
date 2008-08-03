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
import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

import org.castor.cpa.query.Condition;
import org.castor.cpa.query.Field;
import org.castor.cpa.query.Foo;
import org.castor.cpa.query.QueryFactory;
import org.castor.cpa.query.Schema;
import org.castor.cpa.query.SelectQuery;
import org.castor.cpa.query.TemporalType;

/**
 * Junit Test for testing between condition of query objects.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public class TestBetween extends TestCase {
    //--------------------------------------------------------------
    
   private static String _common = "SELECT o.bar FROM org.castor.cpa.query.Foo AS o WHERE ";
   
   //--------------------------------------------------------------
   
    public static void testWithLong() {
        SelectQuery select = QueryFactory.newSelectQuery();
        Schema schema = select.newSchema(Foo.class, "o");
        select.addProjection(schema.field("bar"));
        Field field = schema.field("position");
        Condition condition = field.between(2L, 3L);
        select.setWhere(condition);
        select.addSchema(schema);
        String expected = "o.position BETWEEN 2 AND 3";
        assertEquals(_common + expected, select.toString());
        System.out.println(select.toString());
    }
    
    public static void testWithDouble() {
        SelectQuery select = QueryFactory.newSelectQuery();
        Schema schema = select.newSchema(Foo.class, "o");
        select.addProjection(schema.field("bar"));
        Field field = schema.field("position");
        Condition condition = field.between(2.02d, 4.04d);
        select.setWhere(condition);
        select.addSchema(schema);
        String expected = "o.position BETWEEN 2.02 AND 4.04";
        assertEquals(_common + expected, select.toString());
        System.out.println(select.toString());
    }
    
    public static void testWithBigDecimal() {
        SelectQuery select = QueryFactory.newSelectQuery();
        Schema schema = select.newSchema(Foo.class, "o");
        select.addProjection(schema.field("bar"));
        Field field = schema.field("position");
        Condition condition = field.between(new BigDecimal("12.34"), new BigDecimal("55.55"));
        select.setWhere(condition);
        select.addSchema(schema);
        String expected = "o.position BETWEEN 12.34 AND 55.55";
        assertEquals(_common + expected, select.toString());
        System.out.println(select.toString());
    }
    
    public static void testWithString() {
        SelectQuery select = QueryFactory.newSelectQuery();
        Schema schema = select.newSchema(Foo.class, "o");
        select.addProjection(schema.field("bar"));
        Field field = schema.field("position");
        Condition condition = field.between("LOW", "HIGH");
        select.setWhere(condition);
        select.addSchema(schema);
        String expected = "o.position BETWEEN 'LOW' AND 'HIGH'";
        assertEquals(_common + expected, select.toString());
        System.out.println(select.toString());
    }
    
    public static void testWithDateDate() {
        SelectQuery select = QueryFactory.newSelectQuery();
        Schema schema = select.newSchema(Foo.class, "o");
        select.addProjection(schema.field("bar"));
        Field field = schema.field("position");
        Date h = new Date(1216545874266L);
        Date l = new Date(1206545874266L);
        Condition condition = field.between(TemporalType.DATE, l, h);
        select.setWhere(condition);
        select.addSchema(schema);
        String expected = "o.position BETWEEN '2008-03-26' AND '2008-07-20'";
        assertEquals(_common + expected, select.toString());
        System.out.println(select.toString());
    }
    
    public static void testWithTimeDate() {
        SelectQuery select = QueryFactory.newSelectQuery();
        Schema schema = select.newSchema(Foo.class, "o");
        select.addProjection(schema.field("bar"));
        Field field = schema.field("position");
        Date h = new Date(1216545874266L);
        Date l = new Date(1206545874266L);
        Condition condition = field.between(TemporalType.TIME, l, h);
        select.setWhere(condition);
        select.addSchema(schema);
        String expected = "o.position BETWEEN '23:37:54.266' AND '17:24:34.266'";
        assertEquals(_common + expected, select.toString());
        System.out.println(select.toString());
    }
    
    public static void testWithTimestampDate() {
        SelectQuery select = QueryFactory.newSelectQuery();
        Schema schema = select.newSchema(Foo.class, "o");
        select.addProjection(schema.field("bar"));
        Field field = schema.field("position");
        Date h = new Date(1216545874266L);
        Date l = new Date(1206545874266L);
        Condition condition = field.between(TemporalType.TIMESTAMP, l, h);
        select.setWhere(condition);
        select.addSchema(schema);
        String expected = 
            "o.position BETWEEN '2008-03-26 23:37:54.266' AND '2008-07-20 17:24:34.266'";
        assertEquals(_common + expected, select.toString());
        System.out.println(select.toString());
    }
     
    public static void testWithDateCal() {
        SelectQuery select = QueryFactory.newSelectQuery();
        Schema schema = select.newSchema(Foo.class, "o");
        select.addProjection(schema.field("bar"));
        Field field = schema.field("position");
        Calendar h = Calendar.getInstance();
        h.setTimeInMillis(1216545874266L);
        Calendar l = Calendar.getInstance();
        l.setTimeInMillis(1206545874266L);
        Condition condition = field.between(TemporalType.DATE, l, h);
        select.setWhere(condition);
        select.addSchema(schema);
        String expected = "o.position BETWEEN '2008-03-26' AND '2008-07-20'";
        assertEquals(_common + expected, select.toString());
        System.out.println(select.toString());
    }
    
    public static void testWithTimeCal() {
        SelectQuery select = QueryFactory.newSelectQuery();
        Schema schema = select.newSchema(Foo.class, "o");
        select.addProjection(schema.field("bar"));
        Field field = schema.field("position");
        Calendar h = Calendar.getInstance();
        h.setTimeInMillis(1216545874266L);
        Calendar l = Calendar.getInstance();
        l.setTimeInMillis(1206545874266L);
        Condition condition = field.between(TemporalType.TIME, l, h);
        select.setWhere(condition);
        select.addSchema(schema);
        String expected = "o.position BETWEEN '23:37:54.266' AND '17:24:34.266'";
        assertEquals(_common + expected, select.toString());
        System.out.println(select.toString());
    }
    
    public static void testWithTimestampCal() {
        SelectQuery select = QueryFactory.newSelectQuery();
        Schema schema = select.newSchema(Foo.class, "o");
        select.addProjection(schema.field("bar"));
        Field field = schema.field("position");
        Calendar h = Calendar.getInstance();
        h.setTimeInMillis(1216545874266L);
        Calendar l = Calendar.getInstance();
        l.setTimeInMillis(1206545874266L);
        Condition condition = field.between(TemporalType.TIMESTAMP, l, h);
        select.setWhere(condition);
        select.addSchema(schema);
        String expected = 
            "o.position BETWEEN '2008-03-26 23:37:54.266' AND '2008-07-20 17:24:34.266'";
        assertEquals(_common + expected, select.toString());
        System.out.println(select.toString());
    }

    public static void testWithExpression() {
        SelectQuery select = QueryFactory.newSelectQuery();
        Schema schema = select.newSchema(Foo.class, "o");
        select.addProjection(schema.field("bar"));
        Field field = schema.field("position");
        MockExpression l = new MockExpression();
        MockExpression h = new MockExpression();
        Condition condition = field.between(l, h);
        select.setWhere(condition);
        select.addSchema(schema);
        String expected = "o.position BETWEEN expression AND expression";
        assertEquals(_common + expected, select.toString());
        System.out.println(select.toString());
    }
    
    //--------------------------------------------------------------
}
