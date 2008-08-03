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
package org.castor.cpa.query.object.select;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.castor.cpa.query.object.expression.AbstractExpression;

/**
 * Final class that represents time expression.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public final class TimeExpression extends AbstractExpression {
    //--------------------------------------------------------------------------
    
    /** The time of time expression. */
    private Date _time;

    //--------------------------------------------------------------------------
    
    /**
     * Gets the time of time expression.
     * 
     * @return the time of time expression
     */
    public Date getTime() {
        return _time;
    }


    /**
     * Sets the time of time expression.
     * 
     * @param time the new time of time expression
     */
    public void setTime(final Date time) {
        _time = time;
    }

    //--------------------------------------------------------------------------
    
    /**
     * {@inheritDoc}
     */
    public StringBuilder toString(final StringBuilder sb) {
        SimpleDateFormat sdf =  new SimpleDateFormat("HH:mm:ss.SSS");
        return sb.append('\'').append(sdf.format(_time).toString()).append('\'');
    }

    //--------------------------------------------------------------------------
    
}
