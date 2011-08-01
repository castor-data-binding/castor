/*
 * Copyright 2009 Ahmad Hassan, Ralf Joachim
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
package org.castor.cpa.persistence.sql.keygen;

import java.util.Properties;

import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.persist.spi.PersistenceFactory;

/**
 * Factory for instantiating NoKeyGenerator objects.
 * 
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date$
 */
public final class NoKeyGeneratorFactory implements KeyGeneratorFactory {
    //-----------------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public String getKeyGeneratorName() { return "NOGEN"; }

    /**
     * {@inheritDoc}
     */
    public KeyGenerator getKeyGenerator(final PersistenceFactory factory,
            final Properties params, final int sqlType) throws MappingException {
        return new NoKeyGenerator(factory);
    }

    //-----------------------------------------------------------------------------------
}
