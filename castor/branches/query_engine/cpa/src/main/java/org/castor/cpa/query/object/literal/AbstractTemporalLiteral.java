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

import java.util.Calendar;
import java.util.Date;

/**
 * Abstract immutable base class for temporal literals.
 *
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public abstract class AbstractTemporalLiteral extends AbstractLiteral {
    //--------------------------------------------------------------------------
    
    /** Date value of the temporal literal. */
    private final Date _value;

    //--------------------------------------------------------------------------
    
    /**
     * Construct an abstract temporal literal with given date.
     * 
     * @param value Date value for the temporal literal.
     */
    protected AbstractTemporalLiteral(final Date value) {
        if (value == null) { throw new NullPointerException(); }
        _value = value;
    }
    
    /**
     * Construct an abstract temporal literal with given calendar.
     * 
     * @param value Calendar value for the temporal literal.
     */
    protected AbstractTemporalLiteral(final Calendar value) {
        if (value == null) { throw new NullPointerException(); }
        _value = value.getTime();
    }
    
    //--------------------------------------------------------------------------
    
   /**
     * Get date value of the temporal literal.
     * 
     * @return Date value of the temporal literal.
     */
    public final Date getValue() {
        return _value;
    }

    //--------------------------------------------------------------------------
}
