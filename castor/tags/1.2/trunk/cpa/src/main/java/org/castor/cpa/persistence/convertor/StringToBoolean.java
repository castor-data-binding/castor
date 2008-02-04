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

import org.castor.core.util.Configuration;

/**
 * Convert <code>String</code> to <code>Boolean</code>.
 * 
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7134 $ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1.3
 */
public final class StringToBoolean extends AbstractTypeConvertor {
    //-----------------------------------------------------------------------------------

    /** Char value that represents boolean <code>true</code>. */
    private char _trueValue = 'T';
    
    /** Char value that represents boolean <code>false</code>. */
    private char _falseValue = 'F';
    
    //-----------------------------------------------------------------------------------

    /**
     * Default constructor.
     */
    public StringToBoolean() {
        super(String.class, Boolean.class);
    }

    //-----------------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public void configure(final Configuration configuration) { }
    
    /**
     * {@inheritDoc}
     */
    public void parameterize(final String parameter) {
        if ((parameter == null) || (parameter.length() != 2)) {
            _trueValue = 'T';
            _falseValue = 'F';
        } else {
            _trueValue = Character.toUpperCase(parameter.charAt(1));
            _falseValue = Character.toUpperCase(parameter.charAt(0));
        }
    }
    
    //-----------------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public Object convert(final Object object) {
        String val = (String) object;
        if (val.length() == 1) {
            char c = Character.toUpperCase(val.charAt(0));
            if (c == _trueValue) { return Boolean.TRUE; }
            if (c == _falseValue) { return Boolean.FALSE; }
        }
        
        throw new ClassCastException("Failed to convert string '" + object
                + "' to boolean because it didn't match the expected values '"
                + _trueValue + "'/'" + _falseValue + "' (true/false).");
    }

    //-----------------------------------------------------------------------------------
}
