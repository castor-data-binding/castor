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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Final immutable class that represents a timestamp literal.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public final class TimestampLiteral extends AbstractTemporalLiteral {
    //--------------------------------------------------------------------------

    /** Date format for timestamp literal. */
    private static final DateFormat DF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    
    //--------------------------------------------------------------------------
    
    /**
     * Construct a timestamp literal with given date.
     * 
     * @param value Date value for the timestamp literal.
     */
    public TimestampLiteral(final Date value) {
        super(value);
    }
    
    /**
     * Construct a timestamp literal with given calendar.
     * 
     * @param value Calendar value for the timestamp literal.
     */
    public TimestampLiteral(final Calendar value) {
        super(value);
    }

    //--------------------------------------------------------------------------
    
    /**
     * {@inheritDoc}
     */
    public StringBuilder toString(final StringBuilder sb) {
        return sb.append("TIMESTAMP '").append(DF.format(getValue())).append('\'');
    }

    //--------------------------------------------------------------------------
}
