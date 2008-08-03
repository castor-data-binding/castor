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
package org.castor.cpa.query.object;

import org.castor.cpa.query.Expression;

/**
 * Final immutable class that implements limit of select query.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public final class Limit extends AbstractQueryObject {
    //--------------------------------------------------------------------------
    
    /** The number of records to limit result of select query to. */
    private Expression _limit;
    
    /** The number of records to offset result of select query to. */
    private Expression _offset;
    
    //--------------------------------------------------------------------------

    /**
     * Construct limit without offset.
     * 
     * @param limit The number of records to limit result of select query to.
     */
    public Limit(final Expression limit) {
        this(limit, null);
    }


    /**
     * Construct limit with given offset.
     * 
     * @param limit The number of records to limit result of select query to.
     * @param offset The number of records to offset result of select query to.
     */
    public Limit(final Expression limit, final Expression offset) {
        _limit = limit;
        _offset = offset;
    }

    //--------------------------------------------------------------------------
    
    /**
     * {@inheritDoc}
     */
    public StringBuilder toString(final StringBuilder sb) {
        if (_limit != null) { 
            _limit.toString(sb);
        }
        if (_offset != null) {
            sb.append(" OFFSET ");
            _offset.toString(sb);
        }
        return sb;
    }

    //--------------------------------------------------------------------------
}
