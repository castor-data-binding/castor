/*
 * Copyright 2009 Ralf Joachim, Ahmad Hassan
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
package org.castor.cpa.persistence.sql.query.condition;

import junit.framework.TestCase;

/** 
 * Test if CompareOperator works as expected.
 *
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date$
 */
public final class TestCompareOperator extends TestCase {
    public void testToString() {
        assertEquals("=", CompareOperator.EQ.toString());
        assertEquals("<>", CompareOperator.NE.toString());
        assertEquals(">", CompareOperator.GT.toString());
        assertEquals(">=", CompareOperator.GE.toString());
        assertEquals("<=", CompareOperator.LE.toString());
        assertEquals("<", CompareOperator.LT.toString());
    }

    public void testInvers() {
        assertEquals(CompareOperator.NE, CompareOperator.EQ.inverse());
        assertEquals(CompareOperator.EQ, CompareOperator.NE.inverse());
        assertEquals(CompareOperator.LE, CompareOperator.GT.inverse());
        assertEquals(CompareOperator.LT, CompareOperator.GE.inverse());
        assertEquals(CompareOperator.GT, CompareOperator.LE.inverse());
        assertEquals(CompareOperator.GE, CompareOperator.LT.inverse());
    }
}
