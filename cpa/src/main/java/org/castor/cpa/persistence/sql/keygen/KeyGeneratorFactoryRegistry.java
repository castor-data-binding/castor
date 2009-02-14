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

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import org.castor.core.util.AbstractProperties;
import org.castor.cpa.CPAProperties;

/**
 * Registry for {@link KeyGeneratorFactory} implementations obtained from the Castor properties
 * file and used by the JDO mapping configuration file.
 * 
 * @author <a href="mailto:on AT ibis DOT odessa DOT ua">Oleg Nitz</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public final class KeyGeneratorFactoryRegistry {
    //-----------------------------------------------------------------------------------

    /** Association between key generator name and {@link KeyGeneratorFactory} instances. */
    private Map<String, KeyGeneratorFactory> _factories =
        new Hashtable<String, KeyGeneratorFactory>();

    //-----------------------------------------------------------------------------------

    /**
     * Create a new registry instance of key generator factories. The registry will be initialized
     * with all key generator factories specified through <b>KEYGENERATOR_FACTORIES</b> property
     * of given properties.
     * 
     * @param properties The properties to use.
     */
    public KeyGeneratorFactoryRegistry(final AbstractProperties properties) {
        Object[] objects = properties.getObjectArray(
                CPAProperties.KEYGENERATOR_FACTORIES, properties.getApplicationClassLoader());
        for (int i = 0; i < objects.length; i++) {
            KeyGeneratorFactory factory = (KeyGeneratorFactory) objects[i];
            _factories.put(factory.getKeyGeneratorName(), factory);
        }
    }

    //-----------------------------------------------------------------------------------

    /**
     * Returns a key generator factory with the specified name. Returns null if the named factory
     * is not supported.
     *
     * @param name Name of the key generator factory.
     * @return The {@link KeyGeneratorFactory} or <code>null</code> if no key generator factory
     *         with this name exists.
     */
    public KeyGeneratorFactory getKeyGeneratorFactory(final String name) {
        return _factories.get(name);
    }

    /**
     * Returns an array of names of all the configured key generator factories. The names can be
     * used to obtain a key generator factory from {@link #getKeyGeneratorFactory}.
     *
     * @return Array of names of key generator factories.
     */
    public String[] getKeyGeneratorFactoryNames() {
        Set<String> keyset = _factories.keySet();
        return keyset.toArray(new String[keyset.size()]);
    }

    //-----------------------------------------------------------------------------------
}
