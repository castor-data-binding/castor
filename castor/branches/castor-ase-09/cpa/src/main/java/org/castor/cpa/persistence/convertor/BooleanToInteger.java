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

import org.castor.core.util.AbstractProperties;

/**
 * Convert <code>Boolean</code> to <code>Integer</code>.
 * 
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7134 $ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1.3
 */
public final class BooleanToInteger extends AbstractTypeConvertor {
    //-----------------------------------------------------------------------------------

    /** Integer value to return for boolean <code>true</code>. */
    private Integer _trueValue = new Integer(1);
    
    /** Integer value to return for boolean <code>false</code>. */
    private Integer _falseValue = new Integer(0);
    
    //-----------------------------------------------------------------------------------

    /**
     * Default constructor.
     */
    public BooleanToInteger() {
        super(Boolean.class, Integer.class);
    }

    //-----------------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public void configure(final AbstractProperties properties) { }
    
    /**
     * {@inheritDoc}
     */
    public void parameterize(final String parameter) {
        if ((parameter == null) || (parameter.length() != 1) || (parameter.charAt(0) != '-')) {
            _trueValue = new Integer(1);
        } else {
            _trueValue = new Integer(-1);
        }
    }
    
    //-----------------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public Object convert(final Object object) {
        boolean val = ((Boolean) object).booleanValue();
        return (val ? _trueValue : _falseValue);
    }

    //-----------------------------------------------------------------------------------
}
