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
 * HIGH-LOW key generator factory. The short name of this key generator is "HIGH-LOW".
 * <br/>
 * It uses the following alrorithm: a special sequence table must be in the database which keeps
 * the maximum key values. The name of the sequence table is a mandatory parameter of the key
 * generator, the parameter name is "table". The name of the primary key column of the sequence
 * table and the name of the column in which maximum values are stored are mandatory parameters
 * with the names "key-column" and "value-column", respectively. The key column contains table
 * names, so it must be of a character type (char or varchar). The value column contains primary
 * key values, it must be of a numeric type (numeric or int). Key generator reads the maximum
 * value X for the given table, writes the new value (X + N) to the sequence table and during
 * next N calls returns values X + 1, ..., X + N without database access. Number N called
 * "grab size" is an optional parameter of the key generator, its parameter name is "grab-size",
 * default value is "10". For example, if you want to obtain HIGH-LOW key generator with 3 digits
 * in the LOW part of the key, you should set "grab-size" to "1000".
 *
 * @see HighLowKeyGenerator
 * @author <a href="mailto:on AT ibis DOT odessa DOT ua">Oleg Nitz</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public final class HighLowKeyGeneratorFactory implements KeyGeneratorFactory {
    //-----------------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public String getKeyGeneratorName() { return "HIGH-LOW"; }

    /**
     * {@inheritDoc}
     */
    public KeyGenerator getKeyGenerator(final PersistenceFactory factory,
            final Properties params, final int sqlType) throws MappingException {
        return new HighLowKeyGenerator(factory, params, sqlType);
    }

    //-----------------------------------------------------------------------------------
}