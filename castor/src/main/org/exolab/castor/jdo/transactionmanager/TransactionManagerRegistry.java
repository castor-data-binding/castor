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


package org.exolab.castor.jdo.transactionmanager;


import java.util.Hashtable;


/**
 * Registry for {@link TransactionManagerFactory} implementations.
 * Is used to create no more than one instance of the given type with 
 * the given parameters.
 * 
 * @author <a href="mailto:ferret@frii.com">Bruce Snyder</a>
 * @author <a href="mailto:Werner.Guttmann@morganstanley.com">Werner Guttmann</a>
 * @version $Id$
 */
final class TransactionManagerRegistry
{

    /**
     * Association between {@link TransactionManagerFactory} names (aliases) 
     * and instances.
     */
    private Hashtable  _transactionManagerFactories = new Hashtable();


    /**
     * Returns a key generator with the specified description
     * for the specified persistence factory.
     *
     * @param factory The persistence factory
     * @param desc The key generator description
     * @return The {@link KeyGenerator}
     */
/*
FIXME - Bruce
    public KeyGenerator getKeyGenerator( PersistenceFactory factory,
                                         KeyGeneratorDescriptor desc,
                                         int sqlType,
                                         LogInterceptor logInterceptor )
            throws MappingException
    {
        String keyGenName;
        KeyGeneratorFactory keyGenFactory;
        KeyGenerator keyGen;

        keyGenName = desc.getName() + " " + sqlType;
        keyGen = (KeyGenerator) _keyGens.get( keyGenName );
        if ( keyGen == null ) {
            keyGenFactory = KeyGeneratorFactoryRegistry.getKeyGeneratorFactory(
                    desc.getKeyGeneratorFactoryName() );

            if (keyGenFactory != null) {
                keyGen = keyGenFactory.getKeyGenerator( factory, desc.getParams(), sqlType );
                if ( keyGen != null && logInterceptor != null ) {
                    logInterceptor.message( "Key generator " +
                            desc.getKeyGeneratorFactoryName() +
                            " has been instantiated, parameters: " +
                            desc.getParams() );
                }
            }
            if ( keyGen == null ) {
                /*
                 * Don't throw exception here, just notify and continue without
                 * key generator
                 *-/
                Logger.getSystemLogger().println( Messages.format(
                        "mapping.noKeyGen", desc.getKeyGeneratorFactoryName() ) );
                return null;
            }
            _keyGens.put( keyGenName, keyGen );
        }    
        return keyGen;
    }
*/
}
