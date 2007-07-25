/*
 * Copyright 2007 Ralf Joachim
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
 * $Id: Configuration.java 6907 2007-03-28 21:24:52Z rjoachim $
 */
package org.castor.cpa;

import org.castor.core.CoreConfiguration;
import org.castor.core.util.CastorConfiguration;
import org.castor.core.util.Configuration;
import org.castor.xml.XMLConfiguration;

/**
 * Castor configuration of CPA modul.
 * 
 * @version $Id: Configuration.java,v 1.8 2006/03/08 17:25:52 jens Exp $
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @since 1.1.3
 */
public final class CPAConfiguration extends Configuration {
    //--------------------------------------------------------------------------

    /** Path to Castor configuration of core modul. */
    private static final String FILEPATH = "/org/castor/cpa/";

    /** Name of Castor configuration of core modul. */
    private static final String FILENAME = "castor.cpa.properties";
    
    /** A static configuration instance to be used during migration to a none static one. */
    // TODO Remove property after support for static configuration has been terminated.
    private static Configuration _instance = null;

    //--------------------------------------------------------------------------
    
    /**
     * Get the one and only static CPA configuration.
     * 
     * @return One and only configuration instance for Castor CPA modul.
     * @deprecated Don't limit your applications flexibility by using a static configuration. Use
     *             your own configuration instance created with one of the newInstance() methods
     *             instead.
     */
    // TODO Remove method after support for static configuration has been terminated.
    public static synchronized Configuration getInstance() {
        if (_instance == null) { _instance = newInstance(); }
        return _instance;
    }
    
    /**
     * Factory method for a default CPA configuration instance. Application and domain class
     * loaders will be initialized to the one used to load the Configuration class. The
     * configuration instance returned will be a CastorConfiguration with a CPAConfiguration, a
     * XMLConfiguration and a CoreConfiguration instance as parents. The CastorConfiguration
     * holding user specific properties is the only one that can be modified by put() and remove()
     * methods. CPAConfiguration, XMLConfiguration and CoreConfiguration are responsble to deliver
     * Castor's default values if they have not been overwritten by the user.
     * 
     * @return Configuration instance for Castor CPA modul.
     */
    public static Configuration newInstance() {
        Configuration core = new CoreConfiguration();
        Configuration cpa = new CPAConfiguration(core);
        Configuration xml = new XMLConfiguration(cpa);
        Configuration castor = new CastorConfiguration(xml);
        return castor;
    }
    
    /**
     * Factory method for a CPA configuration instance that uses the specified class loaders. The
     * configuration instance returned will be a CastorConfiguration with a CPAConfiguration, a
     * XMLConfiguration and a CoreConfiguration instance as parents. The CastorConfiguration
     * holding user specific properties is the only one that can be modified by put() and remove()
     * methods. CPAConfiguration, XMLConfiguration and CoreConfiguration are responsble to deliver
     * Castor's default values if they have not been overwritten by the user.
     * 
     * @param app Classloader to be used for all classes of Castor and its required libraries.
     * @param domain Classloader to be used for all domain objects.
     * @return Configuration instance for Castor CPA modul.
     */
    public static Configuration newInstance(final ClassLoader app, final ClassLoader domain) {
        Configuration core = new CoreConfiguration(app, domain);
        Configuration cpa = new CPAConfiguration(core);
        Configuration xml = new XMLConfiguration(cpa);
        Configuration castor = new CastorConfiguration(xml);
        return castor;
    }
    
    //--------------------------------------------------------------------------

    /**
     * Construct a configuration with given parent. Application and domain class loaders will be
     * initialized to the ones of the parent. 
     * <br/>
     * Note: This constructor is not intended for public use. Use one of the newInstance() methods
     * instead.
     * 
     * @param parent Parent configuration.
     */
    public CPAConfiguration(final Configuration parent) {
        super(parent);
        loadDefaultProperties(FILEPATH, FILENAME);
    }
    
    //--------------------------------------------------------------------------
    
    // Specify public keys of xml configuration properties here.
    
    /** Property listing all available {@link org.castor.cache.Cache} implementations 
     *  (<tt>org.castor.cache.Factories</tt>). */
    public static final String CACHE_FACTORIES =
        "org.castor.cache.Factories";

    /** Property listing all the available
     *  {@link org.castor.transactionmanager.TransactionManagerFactory}
     *  implementations (<tt>org.castor.transaction.TransactionManagerFactories</tt>). */
    public static final String TRANSACTION_MANAGER_FACTORIES =
        "org.castor.transactionmanager.Factories";
    
    /** Property telling if TransactionManager should be initialized at registration. */
    public static final String TRANSACTION_MANAGER_INIT = 
        "org.castor.transactionmanager.InitializeAtRegistration";
    
    /** Property telling if database should be initialized when loading. */
    public static final String INITIALIZE_AT_LOAD =
        "org.exolab.castor.jdo.DatabaseInitializeAtLoad";
    
    /** Property name of default timezone in castor.properties. */
    public static final String DEFAULT_TIMEZONE =
        "org.exolab.castor.jdo.defaultTimeZone";
    
    /** Property listing all the available key genence
     *  factories. (<tt>org.exolab.castor.jdo.keyGeneratorFactories</tt>). */
    public static final String KEYGENERATOR_FACTORIES = 
        "org.exolab.castor.jdo.keyGeneratorFactories";

    /** Property name of LOB buffer size in castor.properties. */
    public static final String LOB_BUFFER_SIZE =
        "org.exolab.castor.jdo.lobBufferSize";
    
    /** Property listing all the available persistence
     *  factories. (<tt>org.exolab.castor.jdo.engines</tt>). */
    public static final String PERSISTENCE_FACTORIES = 
        "org.exolab.castor.jdo.engines";

    /** Property listing all the available
     *  {@link org.exolab.castor.persist.TxSynchronizable}
     *  implementations (<tt>org.exolab.castor.persit.TxSynchronizable</tt>). */
    public static final String TX_SYNCHRONIZABLE =
        "org.exolab.castor.persist.TxSynchronizable";

    /** Property specifying whether JDBC 3.0-specific features should be used, 
     *  such as e.g. the use of Statement.getGeneratedKeys() 
     *  <pre>org.castor.jdo.use.jdbc30</pre>. */
    public static final String USE_JDBC30 =
        "org.castor.jdo.use.jdbc30";
    
    /** Property specifying whether JDBC proxy classes should be used 
     *  <pre>org.exolab.castor.persist.useProxies</pre>. */
    public static final String USE_JDBC_PROXIES =
        "org.exolab.castor.persist.useProxies";

    //--------------------------------------------------------------------------
}
