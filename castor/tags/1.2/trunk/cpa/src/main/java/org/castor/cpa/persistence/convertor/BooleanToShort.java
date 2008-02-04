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
 * Convert <code>Boolean</code> to <code>Short</code>.
 * 
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7134 $ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1.3
 */
public final class BooleanToShort extends AbstractTypeConvertor {
    //-----------------------------------------------------------------------------------

    /** Short value to return for boolean <code>true</code>. */
    private Short _trueValue = new Short((short) 1);
    
    /** Short value to return for boolean <code>false</code>. */
    private Short _falseValue = new Short((short) 0);
    
    //-----------------------------------------------------------------------------------

    /**
     * Default constructor.
     */
    public BooleanToShort() {
        super(Boolean.class, Short.class);
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
        if ((parameter == null) || (parameter.length() != 1) || (parameter.charAt(0) != '-')) {
            _trueValue = new Short((short) 1);
        } else {
            _trueValue = new Short((short) -1);
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
