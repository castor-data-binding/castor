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
 * $Id$
 */
package org.castor.cpa.persistence.sql.query.visitor;

import java.util.StringTokenizer;

import org.castor.cpa.persistence.sql.query.QueryConstants;
import org.castor.cpa.persistence.sql.query.Select;

/**
 * Visitor defining special behavior of query building for SapDb database.
 *
 * @author <a href="mailto:madsheepscarer AT googlemail DOT com">Dennis Butterstein</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date$
 */
public final class SapDbQueryVisitor extends DefaultQueryVisitor {
    //-----------------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    protected String quoteName(final String name) {
        StringBuffer buffer = new StringBuffer();
        StringTokenizer tokens = new StringTokenizer(name, ".");
        String token = null;
        boolean addQuote = true;


        buffer.append('\"');
        buffer.append(tokens.nextToken().toUpperCase());

        while (tokens.hasMoreTokens()) {
            token = tokens.nextToken();

            if (isAFunction(token)) {
                addQuote = false;
                buffer.append("\".");
            } else {
                buffer.append("\".\"");
                token = token.toUpperCase();
            }

            buffer.append(token);
        }

        if (addQuote) {
            buffer.append('\"');
        }

        return buffer.toString();
    }

    /**
     * Tests a text string against a known list of functions to determine if it is a function.
     *
     * @param text The text to be checked.
     * @return <code>true</code> if text is a known function name, <code>false</code>
     *         otherwise.
     */
    private boolean isAFunction(final String text) {
        boolean isAFunction = false;

        // Add all supported functions in SAP DB here
        String[] knownFunctions = new String[] {
            "nextval",
            "currval"
        };

        for (int i = 0; i < knownFunctions.length; ++i) {
            String function = knownFunctions[i];

            if (text.equals(function)) {
                return true;
            }
        }

        return isAFunction;
    }

    /**
     * {@inheritDoc}
     */
    protected void handleLock(final Select select) {
        if (select.isLocked()) {
            _queryString.append(QueryConstants.SPACE);
            _queryString.append(QueryConstants.WITH);
            _queryString.append(QueryConstants.SPACE);
            _queryString.append(QueryConstants.LOCK);
        }
    }

    //-----------------------------------------------------------------------------------
}
