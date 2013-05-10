/*
 * Copyright 2009 Oleg Nitz, Ralf Joachim
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
 *
 * $Id$
 */
package org.castor.cpa.persistence.sql.keygen;

import java.util.Properties;

import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.persist.spi.PersistenceFactory;

/**
 * Interface for a key generator factories. The key generator factory is used for producing
 * key generators for concrete databases with given parameters.
 * 
 * @author <a href="mailto:on AT ibis DOT odessa DOT ua">Oleg Nitz</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public interface KeyGeneratorFactory {
    //-----------------------------------------------------------------------------------
    
    /**
     * Get the short name of the key generator. It is used to reference key generators in a
     * mapping configuration file. If several key generators of the same type are used for the
     * same database, then they are referenced by aliases.
     * 
     * @return Name of the {@link KeyGenerator} used to identify key generator (types).
     */
    String getKeyGeneratorName();

    /**
     * Produce the key generator.
     * 
     * @param factory Helper object for obtaining database-specific QuerySyntax.
     * @param params Parameters for key generator.
     * @param sqlType The SQL type of the primary key, the generated identities must have
     *        the corresponding Java type, e.g. java.sql.Types.INTEGER corresponds to
     *        java.lang.Integer, java.sql.Types.NUMERIC corresponds to java.lang.BigDecimal.
     * @return A {@link KeyGenerator} instance.
     * @throws MappingException If there's a problem resolving the mapping information. 
     */
    KeyGenerator getKeyGenerator(PersistenceFactory factory, Properties params, int sqlType)
    throws MappingException;

    //-----------------------------------------------------------------------------------
}
