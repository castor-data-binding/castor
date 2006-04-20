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


package org.exolab.castor.jdo.transactionmanager;


import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import org.exolab.castor.util.Configuration;
import org.exolab.castor.util.LocalConfiguration;
import org.exolab.castor.util.Logger;
import org.exolab.castor.util.Messages;


/**
 * Registry for {@link TransactionManagerFactory} implementations
 * obtained from the Castor properties file and used by the
 * JDO mapping configuration file.
 * 
 * @author <a href="mailto:ferret@frii.com">Bruce Snyder</a>
 * @author <a href="mailto:Werner.Guttmann@morganstanley.com">Werner Guttmann</a>
 * @version $Id$
 */
public final class TransactionManagerFactoryRegistry
{


    /**
     * Property listing all the available {@link TransactionManagerFactory}
     * implementations (<tt>org.exolab.castor.jdo.transactionManagerFactories</tt>).
     */
    private static final String TransactionManagerFactoriesProperty = 
        "org.exolab.castor.jdo.transactionManagerFactories";


    /**
     * Association between {@link TransactionManager} implementation name and 
     * TransactionManagerFactory.
     */
    private static Hashtable  _factories;


    /**
     * Returns a {@link TransactionManagerFactory} with the specified name.
     * The factory class names are loaded from the Castor properties
     * file. Returns null if the named factory is not supported.
     *
     * @param name The TransactionManagerFactory name
     * @return The {@link TransactionManagerFactory}, null
     *  if no TransactionManagerFactory with this name exists
     */
    public static TransactionManagerFactory getTransactionManager( String name )
    {
        load();
        return ( TransactionManagerFactory ) _factories.get( name );
    }


    /**
     * Returns the names of all the configured {@link TransactionManagerFactory}
     * implementations. The names can be used to obtain a {@link TransactionManagerFactory}
     * from {@link #getTransactionManagerFactory}.
     *
     * @return Names of {@link TransactionManagerFactory} implementations
     */
    public static String[] getTransactionManagerFactoryNames()
    {
        String[]    names;
        Enumeration enum;

        load();
        names = new String[ _factories.size() ];
        enum = _factories.keys();
        for ( int i = 0 ; i < names.length ; ++i ) {
            names[ i ] = (String) enum.nextElement();
        }
        return names;
    }


    /**
     * Load the {@link TransactionManagerFactory} implementations from the 
     * properties file, if not loaded before.
     */
    private static synchronized void load()
    {
        if ( _factories == null ) {
            String                    prop;
            StringTokenizer           tokenizer;
            TransactionManagerFactory factory;
            Class                     cls;
            
            _factories = new Hashtable();
            prop = LocalConfiguration.getInstance().getProperty( TransactionManagerFactoriesProperty, "" );
            tokenizer = new StringTokenizer( prop, ", " );
            while ( tokenizer.hasMoreTokens() ) {
                prop = tokenizer.nextToken();
                try {
                    cls = TransactionManagerFactoryRegistry.class.getClassLoader().loadClass( prop );
                    factory = ( TransactionManagerFactory ) cls.newInstance();
                    _factories.put( factory.getName(), factory );
                } catch ( Exception except ) {
                    Logger.getSystemLogger().println( Messages.format( 
                        "transactionManager.missingTransactionManagerFactory", prop ) );
                }
            }
        }
    }


}
