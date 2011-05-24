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
package org.castor.cpa.query.object.literal;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.castor.cpa.query.Expression;
import org.castor.cpa.query.Literal;
import org.castor.cpa.query.QueryObject;
import org.castor.cpa.query.object.expression.AbstractExpression;

import junit.framework.TestCase;

/**
 * Junit Test for testing DateLiteral class.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public final class TestDateLiteral extends TestCase {
    //--------------------------------------------------------------------------
    
    /**
     * Junit Test for instance Date Temporal Literal.
     */
    public void testInstance() throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = df.parse("2008-08-08");
        
        QueryObject n = new DateLiteral(date);
        assertTrue(n instanceof AbstractTemporalLiteral);
        assertTrue(n instanceof AbstractLiteral);
        assertTrue(n instanceof Literal);
        assertTrue(n instanceof AbstractExpression);
        assertTrue(n instanceof Expression);
    }
   
    /**
     * Junit test for constructor.
     */
    public void testConstructor() throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = df.parse("2008-08-08");
        
        AbstractTemporalLiteral tl1 = new DateLiteral(date);
        assertEquals(date, tl1.getValue());

        try {
            new DateLiteral((Date) null);
            fail("NullPointerException should have been thrown");
        } catch (NullPointerException ex) {
            assertTrue(true);
        }
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        
        AbstractTemporalLiteral tl2 = new DateLiteral(cal);
        assertEquals(date, tl2.getValue());
        
        try {
            new DateLiteral((Calendar) null);
            fail("NullPointerException should have been thrown");
        } catch (NullPointerException ex) {
            assertTrue(true);
        }
    } 

    /**
     * Junit Test for Date Literal toString method.
     */
    public void testToString() throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date date = df.parse("2008-08-08 12:34:56.789");
        
        DateLiteral n = new DateLiteral(date);
        assertEquals("DATE '2008-08-08'", n.toString());
    } 
    
    //--------------------------------------------------------------------------
}
