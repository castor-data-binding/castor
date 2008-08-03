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

import org.castor.cpa.query.object.OrderImpl;

/**
 * The Class TestQOImplimentation.
 */
public class TestQOImplimentation extends TestCase {

    /**
     * Instantiates a new junit Test for qo implimentation.
     * 
     * @param name the name
     */
    public TestQOImplimentation(final String name) {
        super(name);
    }

    /**
     * Junit Test for simple select.
     */
    public static void testSimpleSelect() {
        SelectQuery select = QueryFactory.newSelectQuery();
        Schema schema = select.newSchema(Foo.class, "o");
        select.addSchema(schema);
        System.out.println(select.toString());
    }
    
    public static void testDistinctSelect() {
        SelectQuery select = QueryFactory.newSelectQuery();
        Schema schema = select.newSchema(Foo.class, "o");
        select.setDistinct(true);
        select.addSchema(schema);
        System.out.println(select.toString());
    }
    
    
    public static void testProjectionSelect() {
        SelectQuery select = QueryFactory.newSelectQuery();
        Schema schema = select.newSchema(Foo.class, "o");
        select.addProjection(schema.field("bar"));
        select.addSchema(schema);
        System.out.println(select.toString());
    }
    
    
    public static void testOrderBySelect() {
        SelectQuery select = QueryFactory.newSelectQuery();
        Schema schema = select.newSchema(Foo.class, "o");
        select.setDistinct(true);
        select.addProjection(schema.field("bar"));
        select.addSchema(schema);
        Order order = new OrderImpl(schema.field("kit"), OrderDirection.ASCENDING);
        select.setOrder(order);
    }
}
