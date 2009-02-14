/*
 * Copyright 2008 Thomas Fach, Ralf Joachim
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
 * UUID key generator factory. The short name of this key generator is "UUID".
 * <br/>
 * It uses the following alrorithm: The uuid is a combination of the IP address, the current time
 * in milliseconds since 1970 and a static counter. The complete key consists of a 30 character
 * fixed length string.
 * <br/>
 * Brief statement: The IP only exists once during runtime of castor, the current time in
 * milliseconds (updated every 55 mills) is in combination to the IP pretty unique. Considering
 * a static counter will be used a database-wide unique key will be created.
 * 
 * @see UUIDKeyGenerator
 * @author <a href="mailto:thomas DOT fach AT publica DOT de">Thomas Fach</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public final class UUIDKeyGeneratorFactory implements KeyGeneratorFactory {
    //-----------------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public String getKeyGeneratorName() { return "UUID"; }

    /**
     * {@inheritDoc}
     */
    public KeyGenerator getKeyGenerator(final PersistenceFactory factory,
            final Properties params, final int sqlType) throws MappingException {
        return new UUIDKeyGenerator(sqlType);
    }

    //-----------------------------------------------------------------------------------
}