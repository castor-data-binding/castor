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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

import org.castor.cpa.query.Condition;
import org.castor.cpa.query.Expression;
import org.castor.cpa.query.QueryObject;
import org.castor.cpa.query.TemporalType;

/**
 * Junit Test for testing between condition of query objects.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public final class TestBetween extends TestCase {
    // --------------------------------------------------------------------------

    /**
     * Junit Test for instance.
     */
    public void testInstance() {
        QueryObject n = new Between();
        assertTrue(n instanceof SimpleCondition);
        assertTrue(n instanceof AbstractCondition);
        assertTrue(n instanceof Condition);
    }

    /**
     * Junit Test for toString.
     */
    public void testToString() {
        Between n = new Between();

        n.setExpression(new MockExpression());
        n.setHigh(new MockExpression());
        n.setLow(new MockExpression());
        n.setNot(true);
        assertEquals("(expression NOT BETWEEN expression AND expression)", n
                .toString());
        n.setNot(false);

        n.setExpression(new MockExpression());
        n.setHigh(new MockExpression());
        n.setLow(new MockExpression());
        assertEquals("(expression BETWEEN expression AND expression)", n
                .toString());

        n.setExpression(null);
        n.setHigh(new MockExpression());
        n.setLow(new MockExpression());
        assertEquals("( BETWEEN expression AND expression)", n.toString());

        n.setExpression(new MockExpression());
        n.setHigh(null);
        n.setLow(new MockExpression());
        assertEquals("(expression BETWEEN expression AND )", n.toString());

        n.setExpression(new MockExpression());
        n.setHigh(new MockExpression());
        n.setLow(null);
        assertEquals("(expression BETWEEN  AND expression)", n.toString());

        n.setExpression(null);
        n.setHigh(null);
        n.setLow(new MockExpression());
        assertEquals("( BETWEEN expression AND )", n.toString());

        n.setExpression(null);
        n.setHigh(new MockExpression());
        n.setLow(null);
        assertEquals("( BETWEEN  AND expression)", n.toString());

        n.setExpression(new MockExpression());
        n.setHigh(null);
        n.setLow(null);
        assertEquals("(expression BETWEEN  AND )", n.toString());

        n.setExpression(null);
        n.setHigh(null);
        n.setLow(null);
        assertEquals("( BETWEEN  AND )", n.toString());
    }

    /**
     * Junit Test for factory method for BigDecimal.
     */
    public void testFactoryMethodBigDecimal() {
        Expression exp = new MockExpression();
        Condition condition = exp.between(new BigDecimal("55.55"),
                new BigDecimal("54.23"));
        assertEquals("(expression BETWEEN 55.55 AND 54.23)", condition
                .toString());
        condition = exp.notBetween(new BigDecimal("55.55"), new BigDecimal(
                "54.23"));
        assertEquals("(expression NOT BETWEEN 55.55 AND 54.23)", condition
                .toString());
    }

    /**
     * Junit Test for factory method for Double.
     */
    public void testFactoryMethodDouble() {
        Expression exp = new MockExpression();
        Condition condition = exp.between(45.234, 324.234234);
        assertEquals("(expression BETWEEN 45.234 AND 324.234234)", condition
                .toString());
        condition = exp.notBetween(45.234, 324.234234);
        assertEquals("(expression NOT BETWEEN 45.234 AND 324.234234)",
                condition.toString());
    }

    /**
     * Junit Test for factory method for Expression.
     */
    public void testFactoryMethodExpression() {
        Expression exp = new MockExpression();
        Condition condition = exp.between(new MockExpression(),
                new MockExpression());
        assertEquals("(expression BETWEEN expression AND expression)",
                condition.toString());
        condition = exp.notBetween(new MockExpression(), new MockExpression());
        assertEquals("(expression NOT BETWEEN expression AND expression)",
                condition.toString());
    }

    /**
     * Junit Test for factory method for Long.
     */
    public void testFactoryMethodLong() {
        Expression exp = new MockExpression();
        Condition condition = exp.between(342, 2345);
        assertEquals("(expression BETWEEN 342 AND 2345)", condition.toString());
        condition = exp.notBetween(342, 2345);
        assertEquals("(expression NOT BETWEEN 342 AND 2345)", condition
                .toString());
    }

    /**
     * Junit Test for factory method for String.
     */
    public void testFactoryMethodString() {
        Expression exp = new MockExpression();
        Condition condition = exp.between("low", "high");
        assertEquals("(expression BETWEEN 'low' AND 'high')", condition
                .toString());
        condition = exp.notBetween("low", "high");
        assertEquals("(expression NOT BETWEEN 'low' AND 'high')", condition
                .toString());
    }

    /**
     * Junit Test for factory method for TemporalType.
     * 
     * @throws ParseException
     */
    public void testFactoryMethodTemporalType() throws ParseException {
        Expression exp = new MockExpression();
        Date dateLow = new SimpleDateFormat("HH:mm:ss.SSS")
                .parse("12:34:56.789");
        Date dateHigh = new SimpleDateFormat("HH:mm:ss.SSS")
                .parse("23:45:34.456");
        Condition condition = exp.between(TemporalType.TIME, dateLow, dateHigh);
        assertEquals("(expression BETWEEN TIME "
                + "'12:34:56.789' AND TIME '23:45:34.456')", condition
                .toString());
        condition = exp.notBetween(TemporalType.TIME, dateLow, dateHigh);
        assertEquals("(expression NOT BETWEEN TIME "
                + "'12:34:56.789' AND TIME '23:45:34.456')", condition
                .toString());

        dateLow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
                .parse("2007-08-08 12:34:56.789");
        dateHigh = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
                .parse("2008-08-08 12:34:56.789");
        condition = exp.between(TemporalType.TIMESTAMP, dateLow, dateHigh);
        assertEquals(
                "(expression BETWEEN TIMESTAMP '2007-08-08 12:34:56.789' AND TIMESTAMP "
                        + "'2008-08-08 12:34:56.789')", condition.toString());
        condition = exp.notBetween(TemporalType.TIMESTAMP, dateLow, dateHigh);
        assertEquals(
                "(expression NOT BETWEEN TIMESTAMP '2007-08-08 12:34:56.789' AND TIMESTAMP "
                        + "'2008-08-08 12:34:56.789')", condition.toString());

        dateLow = new SimpleDateFormat("yyyy-MM-dd").parse("2007-08-08");
        dateHigh = new SimpleDateFormat("yyyy-MM-dd").parse("2008-08-08");
        condition = exp.between(TemporalType.DATE, dateLow, dateHigh);
        assertEquals("(expression BETWEEN DATE '2007-08-08' AND DATE "
                + "'2008-08-08')", condition.toString());
        condition = exp.notBetween(TemporalType.DATE, dateLow, dateHigh);
        assertEquals("(expression NOT BETWEEN DATE '2007-08-08' AND DATE "
                + "'2008-08-08')", condition.toString());

        dateLow = new SimpleDateFormat("HH:mm:ss.SSS").parse("13:45:34.456");
        dateHigh = new SimpleDateFormat("HH:mm:ss.SSS").parse("23:45:34.456");
        Calendar calLow = Calendar.getInstance();
        calLow.setTime(dateLow);
        Calendar calHigh = Calendar.getInstance();
        calHigh.setTime(dateHigh);
        condition = exp.between(TemporalType.TIME, calLow, calHigh);
        assertEquals("(expression BETWEEN TIME '13:45:34.456' AND TIME "
                + "'23:45:34.456')", condition.toString());
        condition = exp.notBetween(TemporalType.TIME, calLow, calHigh);
        assertEquals("(expression NOT BETWEEN TIME '13:45:34.456' AND TIME "
                + "'23:45:34.456')", condition.toString());

        dateLow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
                .parse("2007-08-08 12:34:56.789");
        dateHigh = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
                .parse("2007-08-08 22:34:56.789");
        calLow = Calendar.getInstance();
        calLow.setTime(dateLow);
        calHigh = Calendar.getInstance();
        calHigh.setTime(dateHigh);
        condition = exp.between(TemporalType.TIMESTAMP, calLow, calHigh);
        assertEquals(
                "(expression BETWEEN TIMESTAMP '2007-08-08 12:34:56.789' AND TIMESTAMP "
                        + "'2007-08-08 22:34:56.789')", condition.toString());
        condition = exp.notBetween(TemporalType.TIMESTAMP, calLow, calHigh);
        assertEquals(
                "(expression NOT BETWEEN TIMESTAMP '2007-08-08 12:34:56.789' AND TIMESTAMP "
                        + "'2007-08-08 22:34:56.789')", condition.toString());

        dateLow = new SimpleDateFormat("yyyy-MM-dd").parse("2007-08-08");
        dateHigh = new SimpleDateFormat("yyyy-MM-dd").parse("2008-08-08");
        calLow = Calendar.getInstance();
        calLow.setTime(dateLow);
        calHigh = Calendar.getInstance();
        calHigh.setTime(dateHigh);
        condition = exp.between(TemporalType.DATE, calLow, calHigh);
        assertEquals("(expression BETWEEN DATE '2007-08-08' AND DATE "
                + "'2008-08-08')", condition.toString());
        condition = exp.notBetween(TemporalType.DATE, calLow, calHigh);
        assertEquals("(expression NOT BETWEEN DATE '2007-08-08' AND DATE "
                + "'2008-08-08')", condition.toString());

    }
    
    
    //  --------------------------------------------------------------
}
