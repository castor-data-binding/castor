/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Intalio, Inc.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Intalio, Inc. Exolab is a registered
 *    trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999-2002 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */


package org.exolab.castor.persist;


import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.castor.util.ConfigKeys;
import org.castor.util.Configuration;
import org.castor.util.Messages;

import org.exolab.castor.persist.spi.KeyGeneratorFactory;


/**
 * Registry for {@link KeyGeneratorFactory} implementations
 * obtained from the Castor properties file and used by the
 * JDO mapping configuration file.
 * 
 * @author <a href="on@ibis.odessa.ua">Oleg Nitz</a>
 * @version $Revision$ $Date$
 */
public final class KeyGeneratorFactoryRegistry
{
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     *  Logging </a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(KeyGeneratorFactoryRegistry.class);
    
    /**
     * Association between key genarator name and KeyGeneratorFactory.
     */
    private static Hashtable  _factories;


    /**
     * Returns a key generator factory with the specified name.
     * The factory class names are loaded from the Castor properties
     * file. Returns null if the named factory is not supported.
     *
     * @param name The key generator name
     * @return The {@link KeyGeneratorFactory}, null
     *  if no key generator factory with this name exists
     */
    public static KeyGeneratorFactory getKeyGeneratorFactory( String name )
    {
        load();
        return (KeyGeneratorFactory) _factories.get( name );
    }


    /**
     * Returns the names of all the configured key generator factories
     * The names can be used to obtain a key generator factory
     * from {@link #getKeyGeneratorFactory}.
     *
     * @return Names of key generator factories
     */
    public static String[] getKeyGeneratorFactoryNames()
    {
        String[]    names;
        Enumeration enumeration;

        load();
        names = new String[ _factories.size() ];
        enumeration = _factories.keys();
        for ( int i = 0 ; i < names.length ; ++i ) {
            names[ i ] = (String) enumeration.nextElement();
        }
        return names;
    }


    /**
     * Load the key generators from the properties file, if not loaded before.
     */
    private static synchronized void load() {
        if (_factories == null) {
            _factories = new Hashtable();
            
            Configuration config = Configuration.getInstance();
            String[] props = config.getProperty(ConfigKeys.KEYGENERATOR_FACTORIES);
            ClassLoader ldr = KeyGeneratorFactoryRegistry.class.getClassLoader();
            for (int i = 0; i < props.length; i++) {
                try {
                    Object factory = ldr.loadClass(props[i]).newInstance();
                    _factories.put(((KeyGeneratorFactory) factory).getName(), factory);
                } catch (Exception except) {
                    LOG.error(Messages.format("persist.missingKeyGeneratorFactory", props[i]));
                }
            }
        }
    }
}
