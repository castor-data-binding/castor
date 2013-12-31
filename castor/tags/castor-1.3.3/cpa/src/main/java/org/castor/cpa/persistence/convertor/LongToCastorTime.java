/*
 * Copyright 2009 Ralf Joachim
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
 * Convert <code>Long</code> to <code>org.exolab.castor.types.Time</code>.
 * 
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.1.3
 */
public final class LongToCastorTime extends AbstractTypeConvertor {
    //-----------------------------------------------------------------------------------
    
    /** Does the long value represent a UTC time? */
    private boolean _utc = false;
    
    //-----------------------------------------------------------------------------------

    /**
     * Default constructor.
     */
    public LongToCastorTime() {
        super(Long.class, org.exolab.castor.types.Time.class);
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
        _utc = ((parameter != null) && ("UTC".equals(parameter.toUpperCase())));
    }
    
    //-----------------------------------------------------------------------------------
    
    /**
     * {@inheritDoc}
     */
    public Object convert(final Object object) {
        return new org.exolab.castor.types.Time(((Long) object).longValue(), _utc);
    }

    //-----------------------------------------------------------------------------------
}
