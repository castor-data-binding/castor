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

import java.sql.SQLException;

import junit.framework.TestCase;

/**
 * Test if QueryContext works as expected.
 * 
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public final class TestQueryContext extends TestCase {
    public void testQuoteNameWithoutFactory() {
        QueryContext ctx = new QueryContext();
        assertEquals("name", ctx.quoteName("name"));
    }

    public void testQuoteNameWithFactory() {
        QueryContext ctx = new QueryContext(new PersistenceFactoryMock());
        assertEquals("'name'", ctx.quoteName("name"));
    }

    public void testGetSequenceNextValStringWithoutFactory() {
        QueryContext ctx = new QueryContext();
        assertEquals("sequence.nextval", ctx.getSequenceNextValString("sequence"));
    }

    public void testGetSequenceNextValStringWithFactory() {
        QueryContext ctx = new QueryContext(new PersistenceFactoryMock());
        assertEquals("NEXTVAL(sequence)", ctx.getSequenceNextValString("sequence"));
    }

    public void testBuilderChar() {
        QueryContext ctx = new QueryContext();
        assertEquals("123", ctx.append('1').append('2').append('3').toString());
    }

    public void testBuilderString() {
        QueryContext ctx = new QueryContext();
        assertEquals("12345", ctx.append("123").append("45").toString());
    }

    public void testParameterHandling() {
        QueryContext ctx = new QueryContext();
        ctx.addParameter("first");
        ctx.addParameter("second");
        ctx.addParameter("third");
        assertEquals(3, ctx.parameterSize());
        
        PreparedStatementMock stmt = PreparedStatementProxy.createPreparedStatementProxy();
        try {
            ctx.bindParameter(stmt, "unknown", null, 0);
            assertEquals(0, stmt.getLastParameterIndex());
            ctx.bindParameter(stmt, "first", null, 0);
            assertEquals(1, stmt.getLastParameterIndex());
            ctx.bindParameter(stmt, "second", null, 0);
            assertEquals(2, stmt.getLastParameterIndex());
            ctx.bindParameter(stmt, "third", null, 0);
            assertEquals(3, stmt.getLastParameterIndex());
            ctx.bindParameter(stmt, "first", null, 0);
            assertEquals(1, stmt.getLastParameterIndex());
        } catch (SQLException ex) {
            fail("no exception should happen here");
        }
    }
}
