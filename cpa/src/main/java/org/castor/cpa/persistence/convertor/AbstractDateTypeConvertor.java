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

import java.text.SimpleDateFormat;

import org.castor.core.util.Configuration;

/**
 * Abstract base class to convert from one type to another without any configuration
 * or parameters.
 * 
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7134 $ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1.3
 */
public abstract class AbstractDateTypeConvertor extends AbstractTypeConvertor {
    //-----------------------------------------------------------------------------------

    /**
     * Date format used by date convertors. Use the {@link #getDefaultDateFormat} accessor to
     * access this variable.
     */
    private static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat();

    //-----------------------------------------------------------------------------------

    /**
     * Use this accessor to access the <tt>DEFAULT_DATE_FORMAT</tt>.
     * 
     * @return A clone of DEFAULT_DATE_FORMAT.
     */
    protected static final SimpleDateFormat getDefaultDateFormat() {
        return (SimpleDateFormat) DEFAULT_DATE_FORMAT.clone();
    }

    /**
     * Transforms short date format pattern into full format pattern for SimpleDateFormat
     * (e.g., "YMD" to "yyyyMMdd").
     *
     * @param pattern The short pattern.
     * @return The full pattern.
     */
    protected static String getFullDatePattern(final String pattern) {
        if (pattern == null) { return "yyyyMMdd"; }
        int len = pattern.length();
        if (len == 0) { return "yyyyMMdd"; }
        
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < len; i++) {
            switch (pattern.charAt(i)) {
            case 'y': case 'Y': sb.append("yyyy"); break;
            case 'M':           sb.append("MM");   break;
            case 'd': case 'D': sb.append("dd");   break;
            case 'h': case 'H': sb.append("HH");   break;
            case 'm':           sb.append("mm");   break;
            case 's':           sb.append("ss");   break;
            case 'S':           sb.append("SSS");  break;
            default:                               break;
            }
        }
        
        return sb.toString();
    }

    //-----------------------------------------------------------------------------------

    /**
     * Construct a Converter between given fromType an toType.
     * 
     * @param fromType The type being converted from.
     * @param toType The type being converted to.
     */
    public AbstractDateTypeConvertor(final Class < ? > fromType, final Class < ? > toType) {
        super(fromType, toType);
    }
    
    //-----------------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public final void configure(final Configuration configuration) { }
    
    //-----------------------------------------------------------------------------------
}
