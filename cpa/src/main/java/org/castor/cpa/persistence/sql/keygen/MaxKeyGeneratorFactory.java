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
import org.exolab.castor.persist.spi.PersistenceFactory;

/**
 * MAX key generator factory. The short name of this key generator is "MAX".
 * <br/>
 * It uses the following alrorithm: the maximum value of the primary key is fetched and the
 * correspondent record is locked until the end of transaction, generator returns (max + 1).
 * The lock guarantees that key generators of concurrent transactions will not use this key
 * value, so DuplicateKeyException is impossible. If the table is empty, generator returns 1,
 * no lock is put, DuplicateKeyException is possible.
 *
 * @see MaxKeyGenerator
 * @author <a href="mailto:on AT ibis DOT odessa DOT ua">Oleg Nitz</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public final class MaxKeyGeneratorFactory implements KeyGeneratorFactory {
    //-----------------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public String getKeyGeneratorName() { return "MAX"; }

    /**
     * {@inheritDoc}
     */
    public KeyGenerator getKeyGenerator(final PersistenceFactory factory,
            final Properties params, final int sqlType) throws MappingException {
        return new MaxKeyGenerator(factory, sqlType);
    }

    //-----------------------------------------------------------------------------------
}