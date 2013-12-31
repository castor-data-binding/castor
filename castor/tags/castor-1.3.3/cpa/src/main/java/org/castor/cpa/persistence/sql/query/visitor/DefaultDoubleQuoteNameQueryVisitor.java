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

/**
 * QueryVisitor defining default method to quote query string.
 *
 * @author <a href="mailto:madsheepscarer AT googlemail DOT com">Dennis Butterstein</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date$
 */
public class DefaultDoubleQuoteNameQueryVisitor extends DefaultQueryVisitor {
    //-----------------------------------------------------------------------------------    

    /**
     * Updated to handle input such as user.tablename.column.
     * 
     * @param name String to be quoted.
     * @return String containing double quotes.
     * @author Andrew Ballanger, 3/15/2001
     */
    protected final String doubleQuoteName(final String name) {
        StringBuffer buffer = new StringBuffer();
        StringTokenizer tokens = new StringTokenizer(name, ".");

        // Note:
        //
        // I am assuming this method always recieves a non null,
        // and non empty string.

        buffer.append('\"');
        buffer.append(tokens.nextToken());
        while (tokens.hasMoreTokens()) {
            buffer.append("\".\"");
            buffer.append(tokens.nextToken());
        }
        buffer.append('\"');

        return buffer.toString();
    }

    //-----------------------------------------------------------------------------------    
}
