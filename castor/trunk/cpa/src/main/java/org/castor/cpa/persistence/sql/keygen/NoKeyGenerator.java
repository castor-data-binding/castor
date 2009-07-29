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

import java.sql.Connection;
import java.util.Properties;

import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.mapping.MappingException;

/**
 * Key generator implementation that does not generate key.
 * 
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 8241 $ $Date: 2006-04-13 06:47:36 -0600 (Thu, 13 Apr 2006) $
 */
public final class NoKeyGenerator extends AbstractNoKeyGenerator {
    //-----------------------------------------------------------------------------------
    
    /**
     * Constructor. 
     */
    public NoKeyGenerator() { }

    /**
     * {@inheritDoc}
     */
    public boolean isInSameConnection() {
        return true;
    }
    
    //-----------------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public Object generateKey(final Connection conn, final String tableName,
            final String primKeyName, final Properties props) throws PersistenceException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String patchSQL(final String insert, final String primKeyName)
            throws MappingException {
        return null;
    }
    
    //-----------------------------------------------------------------------------------
}
