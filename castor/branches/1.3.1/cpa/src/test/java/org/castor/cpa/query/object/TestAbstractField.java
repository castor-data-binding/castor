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

import org.castor.cpa.query.Condition;
import org.castor.cpa.query.Expression;
import org.castor.cpa.query.Field;
import org.castor.cpa.query.InCondition;
import org.castor.cpa.query.QueryObject;
import org.castor.cpa.query.object.condition.In;
import org.castor.cpa.query.object.condition.Null;
import org.castor.cpa.query.object.expression.AbstractExpression;

import junit.framework.TestCase;

/**
 * Junit test for testing AbstractField class.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public final class TestAbstractField extends TestCase {
    //--------------------------------------------------------------------------

    /**
     * Junit Test for instance.
     */
    public void testInstance() {
        QueryObject n = new MockField();
        assertTrue(n instanceof AbstractField);
        assertTrue(n instanceof AbstractExpression);
        assertTrue(n instanceof AbstractQueryObject);
        assertTrue(n instanceof Field);
        assertTrue(n instanceof Expression);
    }

    /**
     * Junit Test field factory method.
     */
    public void testFactoryMethodField() {
        Field parent = new MockField();
        Field field = parent.field("field");
        assertNotNull(field);
        assertTrue(field instanceof FieldImpl);
        assertEquals(parent, ((FieldImpl) field).getParent());
        assertEquals("field", ((FieldImpl) field).getName());
    } 

    /**
     * Junit Test in factory method.
     */
    public void testFactoryMethodIn() {
        Field field = new MockField();
        InCondition in = field.in();
        assertNotNull(in);
        assertTrue(in instanceof In);
        assertFalse(((In) in).isNot());
        assertEquals(field, ((In) in).getExpression());
    } 

    /**
     * Junit Test notIn factory method.
     */
    public void testFactoryMethodNotIn() {
        Field field = new MockField();
        InCondition in = field.notIn();
        assertNotNull(in);
        assertTrue(in instanceof In);
        assertTrue(((In) in).isNot());
        assertEquals(field, ((In) in).getExpression());
    } 

    /**
     * Junit Test isNull factory method.
     */
    public void testFactoryMethodIsNull() {
        Field field = new MockField();
        Condition condition = field.isNull();
        assertNotNull(condition);
        assertTrue(condition instanceof Null);
        assertFalse(((Null) condition).isNot());
        assertEquals(field, ((Null) condition).getExpression());
    } 

    /**
     * Junit Test isNotNull factory method.
     */
    public void testFactoryMethodIsNotNull() {
        Field field = new MockField();
        Condition condition = field.isNotNull();
        assertNotNull(condition);
        assertTrue(condition instanceof Null);
        assertTrue(((Null) condition).isNot());
        assertEquals(field, ((Null) condition).getExpression());
    } 

    //--------------------------------------------------------------------------
}

