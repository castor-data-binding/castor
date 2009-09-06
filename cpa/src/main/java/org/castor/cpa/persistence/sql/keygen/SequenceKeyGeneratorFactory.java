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
 * SEQUENCE key generator factory. The short name of this key generator is "SEQUENCE".
 * <br/>
 * It uses Oracle/PostrgeSQL SEQUENCEs. There are two optional parameters for this
 * key generator:
 * <ul>
 * <li>1) name is "sequence" and the default value is "{0}_seq";</li>
 * <li>2) name is "returning", values: "true"/"false", default is "false".</li>
 * </ul>
 * The latter parameter should be used only with Oracle8i, "true" value turns on more
 * efficient RETURNING syntax. It is possible to use naming patterns like this to
 * obtain SEQUENCE name by table name. This gives the possibility to use one global
 * key generator declaration rather than one per table.
 *
 * @see SequenceKeyGenerator
 * @author <a href="mailto:on AT ibis DOT odessa DOT ua">Oleg Nitz</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public final class SequenceKeyGeneratorFactory implements KeyGeneratorFactory {
    //-----------------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public String getName() { return "SEQUENCE"; }

    /**
     * {@inheritDoc}
     */
    public KeyGenerator getKeyGenerator(final PersistenceFactory factory,
            final Properties params, final int sqlType) throws MappingException {
        return new SequenceKeyGenerator(factory, params, sqlType);
    }

    //-----------------------------------------------------------------------------------
}
