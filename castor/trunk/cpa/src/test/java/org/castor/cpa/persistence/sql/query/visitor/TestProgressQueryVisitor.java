/*
 * Copyright 2010 Dennis Butterstein, Ralf Joachim
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
 *
 * $Id: SQLStatementDelete.java 8469 2009-12-28 16:47:54Z rjoachim $
 */

package org.castor.cpa.persistence.sql.query.visitor;

import org.castor.cpa.persistence.sql.query.Visitor;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test if PostgreSQLQueryVisitor works as expected.
 *
 * @author <a href="mailto:madsheepscarer AT googlemail DOT com">Dennis Butterstein</a>
 * @version $Revision: 8469 $ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public final class TestProgressQueryVisitor extends TestPostgreSQLQueryVisitor {
    //-----------------------------------------------------------------------------------

    @Test
    public void testQuoteName() throws Exception {
        Visitor queryVis = getVisitor();

        String expected = "TestName"; 

        assertEquals(("\"" + expected + "\""),
                ((ProgressQueryVisitor) queryVis).quoteName(expected));
    }

    @Test
    public void testGetSequenceNextValString() throws Exception {
        Visitor queryVis = getVisitor();

        String name = "TestName"; 

        assertEquals(null, ((ProgressQueryVisitor) queryVis).getSequenceNextValString(name));
    }

    protected Visitor getVisitor() {
        return new ProgressQueryVisitor();
    }

    //-----------------------------------------------------------------------------------
}
