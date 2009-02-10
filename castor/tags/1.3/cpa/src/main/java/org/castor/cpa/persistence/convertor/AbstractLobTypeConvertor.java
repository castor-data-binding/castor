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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.util.AbstractProperties;
import org.castor.cpa.CPAProperties;

/**
 * Abstract base class to convert from one type to another using a large object buffer
 * whose size can be configured through properties file. The convertors do not have any
 * parameters.
 * 
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7134 $ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1.3
 */
public abstract class AbstractLobTypeConvertor extends AbstractTypeConvertor {
    //-----------------------------------------------------------------------------------

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(AbstractLobTypeConvertor.class);
    
    /** Default value of LOB buffer size if property could not been loaded. */
    private static final int DEFAULT_LOB_BUFFER_SIZE = 256;

    /** LOB buffer size. */
    private int _lobBufferSize = -1;

    //-----------------------------------------------------------------------------------

    /**
     * Construct a Converter between given fromType an toType.
     * 
     * @param fromType The type being converted from.
     * @param toType The type being converted to.
     */
    public AbstractLobTypeConvertor(final Class < ? > fromType, final Class < ? > toType) {
        super(fromType, toType);
    }
    
    //-----------------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public final void configure(final AbstractProperties properties) {
        _lobBufferSize = properties.getInteger(
                CPAProperties.LOB_BUFFER_SIZE, DEFAULT_LOB_BUFFER_SIZE);
        if (LOG.isDebugEnabled()) { LOG.debug("Using lobBufferSize: " + _lobBufferSize); }
    }
    
    /**
     * {@inheritDoc}
     */
    public final void parameterize(final String parameter) { }
    
    //-----------------------------------------------------------------------------------

    /**
     * Get LOB buffer size.
     * 
     * @return LOB buffer size.
     */
    protected final int getLobBufferSize() {
        return _lobBufferSize;
    }

    //-----------------------------------------------------------------------------------
}
