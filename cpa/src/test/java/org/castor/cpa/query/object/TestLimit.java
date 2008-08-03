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

import org.castor.cpa.query.Foo;
import org.castor.cpa.query.Parameter;
import org.castor.cpa.query.QueryFactory;
import org.castor.cpa.query.Schema;
import org.castor.cpa.query.SelectQuery;

/**
 * Junit test for testing limit query object.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public class TestLimit extends TestCase {
   
    private static String _common = "SELECT o.bar FROM org.castor.cpa.query.Foo AS o ";
    
    public static void testWithLimitLong() {
        SelectQuery select = QueryFactory.newSelectQuery();
        Schema schema = select.newSchema(Foo.class, "o");
        select.addProjection(schema.field("bar"));
        select.addSchema(schema);
        select.setLimit(4);
        String expected = "LIMIT 4";
        assertEquals(_common + expected, select.toString());
        System.out.println(select.toString());
    }
    
    public static void testWithLimitParam() {
        SelectQuery select = QueryFactory.newSelectQuery();
        Schema schema = select.newSchema(Foo.class, "o");
        select.addProjection(schema.field("bar"));
        select.addSchema(schema);
        Parameter param = new MockParameter();
        select.setLimit(param);
        String expected = "LIMIT Parameter";
        assertEquals(_common + expected, select.toString());
        System.out.println(select.toString());
    }
    
    public static void testWithLimitLongOffsetLong() {
        SelectQuery select = QueryFactory.newSelectQuery();
        Schema schema = select.newSchema(Foo.class, "o");
        select.addProjection(schema.field("bar"));
        select.addSchema(schema);
        select.setLimit(4, 6);
        String expected = "LIMIT 4 OFFSET 6";
        assertEquals(_common + expected, select.toString());
        System.out.println(select.toString());
    }
    
    
    public static void testWithLimitParamOffsetParam() {
        SelectQuery select = QueryFactory.newSelectQuery();
        Schema schema = select.newSchema(Foo.class, "o");
        select.addProjection(schema.field("bar"));
        select.addSchema(schema);
        Parameter param1 = new MockParameter();
        Parameter param2 = new MockParameter();
        select.setLimit(param1, param2);
        String expected = "LIMIT Parameter OFFSET Parameter";
        assertEquals(_common + expected, select.toString());
        System.out.println(select.toString());
    }
}
