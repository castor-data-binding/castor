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
package org.castor.cpa.query.object.function;

import junit.framework.TestCase;

import org.castor.cpa.query.Expression;
import org.castor.cpa.query.Function;
import org.castor.cpa.query.QueryObject;
import org.castor.cpa.query.TrimSpecification;
import org.castor.cpa.query.object.expression.AbstractExpression;
import org.castor.cpa.query.object.literal.StringLiteral;

/**
 * Junit Test for testing TRIM function class of query objects.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public final class TestTrim extends TestCase {
    //--------------------------------------------------------------------------

    /**
     * Junit Test for instance.
     */
    public void testInstance() {
        QueryObject n = new Trim();
        assertTrue(n instanceof AbstractFunction);
        assertTrue(n instanceof Function);
        assertTrue(n instanceof AbstractExpression);
        assertTrue(n instanceof Expression);
    }

    /**
     * Junit Test for constructor.
     */
    public void testConstructor() {
        Trim n = new Trim();
        assertEquals(TrimSpecification.BOTH, n.getSpecification());
        assertTrue(n.getCharacter() instanceof StringLiteral);
        assertEquals(" ", ((StringLiteral) n.getCharacter()).getValue());
    }

    /**
     * Junit Test for Getter and Setter methods.
     */
    public void testGSetter() {
        Expression exp = new MockExpression();
        TrimSpecification t = TrimSpecification.LEADING;
        StringLiteral c = new StringLiteral("Char");

        Trim n = new Trim();
        n.setString(exp);
        n.setSpecification(t);
        n.setCharacter(c);

        assertEquals(exp, n.getString());
        assertEquals(t, n.getSpecification());
        assertEquals(c, n.getCharacter());
    }

    /**
     * Junit Test for toString method.
     */
    public void testToString() {

        Trim n = new Trim();

        n.setString(null);
        n.setSpecification(TrimSpecification.LEADING);
        n.setCharacter(new StringLiteral("Char"));
        assertEquals("TRIM(LEADING 'Char' FROM )", n.toString());

        n.setString(new MockExpression());
        n.setSpecification(null);
        n.setCharacter(new StringLiteral("Char"));
        assertEquals("TRIM( 'Char' FROM expression)", n.toString());

        n.setString(new MockExpression());
        n.setSpecification(TrimSpecification.LEADING);
        n.setCharacter(null);
        assertEquals("TRIM(LEADING  FROM expression)", n.toString());

        n.setString(new MockExpression());
        n.setSpecification(null);
        n.setCharacter(null);
        assertEquals("TRIM(  FROM expression)", n.toString());

        n.setString(null);
        n.setSpecification(TrimSpecification.LEADING);
        n.setCharacter(null);
        assertEquals("TRIM(LEADING  FROM )", n.toString());

        n.setString(null);
        n.setSpecification(null);
        n.setCharacter(new StringLiteral("Char"));
        assertEquals("TRIM( 'Char' FROM )", n.toString());

        n.setString(null);
        n.setSpecification(null);
        n.setCharacter(null);
        assertEquals("TRIM(  FROM )", n.toString());

        // withChar false and withSpec false
        n.setString(new MockExpression());
        n.setSpecification(TrimSpecification.BOTH);
        n.setCharacter(new StringLiteral(" "));
        assertEquals("TRIM(expression)", n.toString());

        // withChar true and withSpec false
        n.setString(new MockExpression());
        n.setSpecification(TrimSpecification.BOTH);
        n.setCharacter(new StringLiteral("Char"));
        assertEquals("TRIM('Char' FROM expression)", n.toString());

        // withChar false and withSpec true
        n.setString(new MockExpression());
        n.setSpecification(TrimSpecification.TRAILING);
        n.setCharacter(new StringLiteral(" "));
        assertEquals("TRIM(TRAILING FROM expression)", n.toString());
    }

    //--------------------------------------------------------------------------
}
