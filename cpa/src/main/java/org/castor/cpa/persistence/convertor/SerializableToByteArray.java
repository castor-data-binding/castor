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

import java.io.Serializable;

/**
 * Convert <code>Serializable</code> to <code>byte[]</code>.
 * <br/>
 * Because Serializable need the right ClassLoader when serializing, we convert
 * to byte[] or InputStream and vice-versa.
 * 
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7134 $ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1.3
 */
public final class SerializableToByteArray extends AbstractSimpleTypeConvertor {
    //-----------------------------------------------------------------------------------

    /**
     * Default constructor.
     */
    public SerializableToByteArray() {
        super(Serializable.class, byte[].class);
    }

    //-----------------------------------------------------------------------------------
    
    /**
     * {@inheritDoc}
     */
    public Object convert(final Object object) {
        return object;
    }

    //-----------------------------------------------------------------------------------
}
