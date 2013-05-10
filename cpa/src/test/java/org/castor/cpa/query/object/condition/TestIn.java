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
import java.util.List;

import junit.framework.TestCase;

import org.castor.cpa.query.Condition;
import org.castor.cpa.query.Expression;
import org.castor.cpa.query.InCondition;
import org.castor.cpa.query.Literal;
import org.castor.cpa.query.Parameter;
import org.castor.cpa.query.QueryObject;
import org.castor.cpa.query.object.literal.BigDecimalLiteral;
import org.castor.cpa.query.object.literal.BooleanLiteral;
import org.castor.cpa.query.object.literal.DoubleLiteral;
import org.castor.cpa.query.object.literal.EnumLiteral;
import org.castor.cpa.query.object.literal.LongLiteral;
import org.castor.cpa.query.object.literal.StringLiteral;

/**
 * Junit Test for testing in condition of query objects.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public final class TestIn extends TestCase {
    //--------------------------------------------------------------------------
    
    /**
     * Junit Test for instance.
     */
    public void testInstance() {
        QueryObject n = new In();
        assertTrue(n instanceof SimpleCondition);
        assertTrue(n instanceof AbstractCondition);
        assertTrue(n instanceof InCondition);
        assertTrue(n instanceof Condition);
     }

    /**
     * Junit Test for factory methods.
     */
    public void testItems() {
        BigDecimal bigDecimal = new BigDecimal("34.67");
        Literal literal = new StringLiteral("stringLiteral");
        Parameter parameter = new MockParameter();

        In n = new In();
        n.add(bigDecimal);
        n.add(true);
        n.add(5.6);
        n.add(MockEnum.TEST2);
        n.add(literal);
        n.add(342);
        n.add(parameter);
        n.add("string");
        
        List < Expression > list = n.getItems();
        assertEquals(bigDecimal, ((BigDecimalLiteral) list.get(0)).getValue());
        assertEquals(true, ((BooleanLiteral) list.get(1)).getValue());
        assertEquals(5.6, ((DoubleLiteral) list.get(2)).getValue());
        assertEquals(MockEnum.TEST2, ((EnumLiteral) list.get(3)).getValue());
        assertEquals(literal, list.get(4));
        assertEquals(342, ((LongLiteral) list.get(5)).getValue());
        assertEquals(parameter, list.get(6));
        assertEquals("string", ((StringLiteral) list.get(7)).getValue());
    }

    /**
     * Junit Test for toString.
     */
    public void testToString() {
        BigDecimal bigDecimal = new BigDecimal("34.67");
        Literal literal = new StringLiteral("stringLiteral");
        Parameter parameter = new MockParameter();

        In n = new In();
        n.setExpression(new MockExpression());
        n.add(bigDecimal);
        n.add(true);
        n.add(5.6);
        n.add(MockEnum.TEST2);
        n.add(literal);
        n.add(342);
        n.add(parameter);
        n.add("string");
        assertEquals("(expression IN (34.67, true, 5.6, " 
                + "org.castor.cpa.query.object.condition.MockEnum.TEST2, " 
                + "'stringLiteral', 342, parameter, 'string'))", n.toString());
     }
    
    //--------------------------------------------------------------------------
}
