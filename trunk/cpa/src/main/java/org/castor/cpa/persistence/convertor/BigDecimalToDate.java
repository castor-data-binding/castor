/*
 * Copyright 2007 Ralf Joachim
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
package org.castor.cpa.persistence.convertor;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Convert <code>BigDecimal</code> to <code>Date</code>.
 * 
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.1.3
 */
public final class BigDecimalToDate extends AbstractDateTypeConvertor {
    //-----------------------------------------------------------------------------------

    /**
     * Date format used by this date convertor. Use the {@link #getCustomDateFormat} accessor
     * to access this variable.
     */
    private SimpleDateFormat _customDateFormat;

    //-----------------------------------------------------------------------------------

    /**
     * Default constructor.
     */
    public BigDecimalToDate() {
        super(BigDecimal.class, Date.class);
        
        parameterize(null);
    }

    //-----------------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public void parameterize(final String parameter) {
        _customDateFormat = getDefaultDateFormat();
        _customDateFormat.applyPattern(getFullDatePattern(parameter));
    }
    
    /**
     * Use this accessor to access the custom date format property.
     * 
     * @return A clone of the custom date format property.
     */
    private SimpleDateFormat getCustomDateFormat() {
        return (SimpleDateFormat) _customDateFormat.clone();
    }

    //-----------------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public Object convert(final Object object) {
        try {
            return getCustomDateFormat().parse(object.toString());
        } catch (ParseException ex) {
            throw new IllegalArgumentException(ex.toString());
        }
    }

    //-----------------------------------------------------------------------------------
}
