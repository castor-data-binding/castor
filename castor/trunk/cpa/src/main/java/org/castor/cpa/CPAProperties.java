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

import org.castor.core.CoreProperties;
import org.castor.core.util.CastorProperties;
import org.castor.core.util.AbstractProperties;
import org.castor.xml.XMLProperties;

/**
 * Properties of CPA modul.
 * 
 * @version $Id: Configuration.java,v 1.8 2006/03/08 17:25:52 jens Exp $
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @since 1.1.3
 */
public final class CPAProperties extends AbstractProperties {
    //--------------------------------------------------------------------------

    /** Path to Castor properties of core modul. */
    private static final String FILEPATH = "/org/castor/cpa/";

    /** Name of Castor properties of core modul. */
    private static final String FILENAME = "castor.cpa.properties";
    
    /** A static properties instance to be used during migration to a none static one. */
    // TODO Remove property after support for static configuration has been terminated.
    private static AbstractProperties _instance = null;

    //--------------------------------------------------------------------------
    
    /**
     * Get the one and only static CPA properties.
     * 
     * @return One and only properties instance for Castor CPA modul.
     * @deprecated Don't limit your applications flexibility by using static properties. Use
     *             your own properties instance created with one of the newInstance() methods
     *             instead.
     */
    // TODO Remove method after support for static properties has been terminated.
    public static synchronized AbstractProperties getInstance() {
        if (_instance == null) { _instance = newInstance(); }
        return _instance;
    }
    
    /**
     * Factory method for a default CPA properties instance. Application and domain class
     * loaders will be initialized to the one used to load this class. The properties instance
     * returned will be a CastorProperties with a CPAProperties, a XMLProperties and a
     * CoreProperties instance as parents. The CastorProperties holding user specific properties
     * is the only one that can be modified by put() and remove() methods. CPAProperties,
     * XMLProperties and CoreProperties are responsble to deliver Castor's default values if they
     * have not been overwritten by the user.
     * 
     * @return Properties instance for Castor CPA modul.
     */
    public static AbstractProperties newInstance() {
        AbstractProperties core = new CoreProperties();
        AbstractProperties cpa = new CPAProperties(core);
        AbstractProperties xml = new XMLProperties(cpa);
        AbstractProperties castor = new CastorProperties(xml);
        return castor;
    }
    
    /**
     * Factory method for a CPA properties instance that uses the specified class loaders. The
     * properties instance returned will be a CastorProperties with a CPAProperties, a
     * XMLProperties and a CoreProperties instance as parents. The CastorProperties
     * holding user specific properties is the only one that can be modified by put() and remove()
     * methods. CPAProperties, XMLProperties and CoreProperties are responsble to deliver
     * Castor's default values if they have not been overwritten by the user.
     * 
     * @param app Classloader to be used for all classes of Castor and its required libraries.
     * @param domain Classloader to be used for all domain objects.
     * @return Properties instance for Castor CPA modul.
     */
    public static AbstractProperties newInstance(final ClassLoader app, final ClassLoader domain) {
        AbstractProperties core = new CoreProperties(app, domain);
        AbstractProperties cpa = new CPAProperties(core);
        AbstractProperties xml = new XMLProperties(cpa);
        AbstractProperties castor = new CastorProperties(xml);
        return castor;
    }
    
    //--------------------------------------------------------------------------

    /**
     * Construct properties with given parent. Application and domain class loaders will be
     * initialized to the ones of the parent. 
     * <br/>
     * Note: This constructor is not intended for public use. Use one of the newInstance() methods
     * instead.
     * 
     * @param parent Parent properties.
     */
    public CPAProperties(final AbstractProperties parent) {
        super(parent);
        loadDefaultProperties(FILEPATH, FILENAME);
    }
    
    //--------------------------------------------------------------------------
    
    // Specify public keys of CPA configuration properties here.
    
    /** Property listing all available {@link org.castor.cache.Cache} implementations 
     *  (<tt>org.castor.cache.Factories</tt>). */
    public static final String CACHE_FACTORIES =
        "org.castor.cache.Factories";

    /** Property listing all available {@link org.castor.cpa.persistence.convertor.TypeConvertor}
     *  implementations (<tt>org.castor.cpa.persistence.TypeConvertors</tt>). */
    public static final String TYPE_CONVERTORS =
        "org.castor.cpa.persistence.TypeConvertors";

    /** Property listing all the available
     *  {@link org.castor.transactionmanager.TransactionManagerFactory}
     *  implementations (<tt>org.castor.transactionmanager.Factories</tt>). */
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
    
    /** Property listing all the available key genence factories.
     *  (<tt>org.castor.cpa.persistence.sql.keygen.factories</tt>). */
    public static final String KEYGENERATOR_FACTORIES = 
        "org.castor.cpa.persistence.sql.keygen.factories";

    /** Property name of LOB buffer size in castor.properties. */
    public static final String LOB_BUFFER_SIZE =
        "org.exolab.castor.jdo.lobBufferSize";
    
    /** Property listing all the available persistence factories.
     *  (<tt>org.castor.cpa.persistence.sql.driver.factories</tt>). */
    public static final String PERSISTENCE_FACTORIES = 
        "org.castor.cpa.persistence.sql.driver.factories";

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

    /** Property specifying whether to use ANSI-compliant SQL for MS SQL Server.
     *  <pre>org.exolab.castor.jdo.sqlserver.ansi-compliant</pre> */
    public static final String MSSQL_ANSI_COMPLIANT =
        "org.exolab.castor.jdo.sqlserver.ansi-compliant";

    //--------------------------------------------------------------------------
}
