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
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.jdo.engine;

import java.util.Properties;

/**
 * Describes the properties of a key generator for a given class, 
 * with resolved alias and parameters.
 *
 * @author <a href="on@ibis.odessa.ua">Oleg Nitz</a>
 * @version $Revision$ $Date: 2003-03-03 00:05:44 -0700 (Mon, 03 Mar 2003) $
 */
public final class KeyGeneratorDescriptor {
    
    /**
     * Name of the key generator (could be an alias).
     */
    private final String _name;

    /**
     * Name of the key generator (type); if an alias is defined, this
     * is the type of the key generator used.
     */
    private final String _keyGenFactoryName;

    /**
     * Configuration parameters for the key generator.
     */
    private final Properties _params;

    /**
     * Creates an instance of this clas.
     * @param name Key generator name (could be an alias) 
     * @param keyGenFactoryName (key generator (type) name.
     * @param params Key generator parameters
     */
    public KeyGeneratorDescriptor(final String name, 
            final String keyGenFactoryName,
            final Properties params) {
        _name = name;
        _keyGenFactoryName = keyGenFactoryName;
        _params = params;
    }

    /**
     * Returns the name of the key generator, i.e. the name of the key
     * generator factory or the alias if it is used.
     *
     * @return Key generator name.
     */
    public String getName() {
        return _name;
    }


    /**
     * Returns the name of the key generator factory.
     *
     * @return Key generator factory name
     */
    public String getKeyGeneratorFactoryName() {
        return _keyGenFactoryName;
    }


    /**
     * Returns the key generator parameters.
     *
     * @return key generator parameters.
     */
    public Properties getParams() {
        return _params;
    }
}

