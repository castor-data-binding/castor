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
package org.castor.cpa.persistence.sql.query;

import junit.framework.TestCase;

/**
 * Test string constants for SQL queries.
 * 
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public final class TestQueryConstants extends TestCase {
    public void testConstants() {
        assertEquals("SELECT", QueryConstants.SELECT);
        assertEquals("DELETE", QueryConstants.DELETE);
        assertEquals("UPDATE", QueryConstants.UPDATE);
        assertEquals("INSERT", QueryConstants.INSERT);
        assertEquals("VALUES", QueryConstants.VALUES);
        assertEquals("INTO", QueryConstants.INTO);
        assertEquals('*', QueryConstants.STAR);
        assertEquals("SET", QueryConstants.SET);
        assertEquals("FROM", QueryConstants.FROM);
        assertEquals("WHERE", QueryConstants.WHERE);
        assertEquals("AND", QueryConstants.AND);
        assertEquals("OR", QueryConstants.OR);
        assertEquals("NOT", QueryConstants.NOT);
        assertEquals("IS", QueryConstants.IS);
        assertEquals("NULL", QueryConstants.NULL);
        assertEquals(' ', QueryConstants.SPACE);
        assertEquals('.', QueryConstants.DOT);
        assertEquals('?', QueryConstants.PARAMETER);
        assertEquals('(', QueryConstants.LPAREN);
        assertEquals(')', QueryConstants.RPAREN);
        assertEquals('=', QueryConstants.ASSIGN);
        assertEquals(',', QueryConstants.SEPERATOR);
    }
}
