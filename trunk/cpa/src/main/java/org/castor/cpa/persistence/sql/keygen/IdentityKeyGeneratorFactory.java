/*
 * Copyright 2008 Oleg Nitz, Ralf Joachim
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
import org.exolab.castor.persist.spi.KeyGenerator;
import org.exolab.castor.persist.spi.KeyGeneratorFactory;
import org.exolab.castor.persist.spi.PersistenceFactory;

/**
 * IDENTITY key generator factory. The short name of this key generator is "IDENTITY".
 * <br/>
 * It works for Sybase and SQL Server identity (autoincrement) fields and fetched
 * @@identity after insert.
 *
 * @see IdentityKeyGenerator
 * @author <a href="mailto:on AT ibis DOT odessa DOT ua">Oleg Nitz</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public final class IdentityKeyGeneratorFactory implements KeyGeneratorFactory {
    //-----------------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public String getName() { return "IDENTITY"; }

    /**
     * {@inheritDoc}
     */
    public KeyGenerator getKeyGenerator(final PersistenceFactory factory,
            final Properties params, final int sqlType) throws MappingException {
        
        return new IdentityKeyGenerator(factory, sqlType);
    }

    //-----------------------------------------------------------------------------------
}
