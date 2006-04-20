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


import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.StringTokenizer;

import org.exolab.castor.jdo.conf.Param;
import org.exolab.castor.jdo.conf.TransactionDemarcation;
import org.exolab.castor.jdo.conf.TransactionManager;
import org.exolab.castor.jdo.engine.JDOConfLoader;
import org.exolab.castor.jdo.transactionmanager.spi.LocalTransactionManagerFactory;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.util.LocalConfiguration;
import org.exolab.castor.util.Logger;
import org.exolab.castor.util.Messages;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;


/**
 * Registry for {@link TransactionManagerFactory} implementations
 * obtained from the Castor properties file and used by the
 * JDO mapping configuration file.
 * 
 * @author <a href="mailto:ferret@frii.com">Bruce Snyder</a>
 * @author <a href="mailto:Werner.Guttmann@morganstanley.com">Werner Guttmann</a>
 * @version $Id$
 */
public final class TransactionManagerFactoryRegistry {


    /**
     * Property listing all the available {@link TransactionManagerFactory}
     * implementations (<tt>org.exolab.castor.jdo.transactionManagerFactories</tt>).
     */
    private static final String TransactionManagerFactoriesProperty = 
        "org.exolab.castor.jdo.spi.transactionManagerFactories";


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
    public static TransactionManagerFactory getTransactionManagerFactory( String name )
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
	 * Returns the names of all the configured {@link TransactionManagerFactory}
	 * implementations. The names can be used to obtain a {@link TransactionManagerFactory}
	 * from {@link #getTransactionManagerFactory}.
	 *
	 * @return Names of {@link TransactionManagerFactory} implementations
	 */
	public static Collection getTransactionManagerFactories() {
		load();
		return Collections.unmodifiableCollection(_factories.keySet());
	}


	/**
	 * Loads configuration related to transaction demarcation and transaction manager factories from JDO config file.
	 * @param source InputSource pointing to the JDO config file.
	 * @param resolver Custom EntityResolver instance.
	 * @throws TransactionManagerAcquireException If there is a problem obtaining tthe configuration. 
	 */
	public static void load (InputSource source, EntityResolver resolver) 
		throws TransactionManagerAcquireException
	{
		load();
		
		try {
			// Load the JDO configuration file from the specified input source.
			TransactionDemarcation transactionDemarcation = JDOConfLoader.getTransactionDemarcation (source, resolver);
				
			if (transactionDemarcation == null)
				throw new TransactionManagerAcquireException ("Problem obtaining transaction manager demarcation configuration");

			String demarcationMode = transactionDemarcation.getMode();
			TransactionManager transactionManager = transactionDemarcation.getTransactionManager();
			
			if (transactionManager == null) {
				
				if (!demarcationMode.equals(LocalTransactionManagerFactory.NAME))
					throw new TransactionManagerAcquireException ("Problem obtaining required transaction manager configuration.");
				
			} else {
				
				String mode = transactionManager.getName();
				if (mode == null)
					throw new TransactionManagerAcquireException ("Attribute MODE for <transaction-manager> required");
			
				TransactionManagerFactory factory = getTransactionManagerFactory(mode);
				if (factory == null)
					throw new TransactionManagerAcquireException ("Invalid value for MODE. Transaction manager factory with MODE = " + 
						mode + "does not exist");

				Properties properties = new Properties();
					
				Enumeration parameters = transactionManager.enumerateParam();
				while (parameters.hasMoreElements()) {
					Param param = (Param) parameters.nextElement();
					properties.put(param.getName(), param.getValue());
				}
			
				factory.setParams(properties);
				
			}
			
			
		}
		catch (MappingException e) {
			throw new TransactionManagerAcquireException ("Problem obtaining JDO configuration", e);
		}

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
