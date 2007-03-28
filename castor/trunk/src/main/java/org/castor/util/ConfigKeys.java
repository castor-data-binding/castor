/*
 * Copyright 2006 Ralf Joachim
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
package org.castor.util;

/**
 * Keys of all Castor configuration properties.
 * 
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-08 08:58:10 -0600 (Sat, 08 Apr 2006) $
 * @since 1.0.1
 */
public final class ConfigKeys {
    /** Property listing all available {@link org.castor.cache.Cache} implementations 
     *  (<tt>org.castor.cache.Factories</tt>). */
    public static final String CACHE_FACTORIES =
        "org.castor.cache.Factories";

    /** Property listing all the available
     * {@link org.castor.transactionmanager.TransactionManagerFactory}
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
     * {@link org.exolab.castor.persist.TxSynchronizable}
     *  implementations (<tt>org.exolab.castor.persit.TxSynchronizable</tt>). */
    public static final String TX_SYNCHRONIZABLE =
        "org.exolab.castor.persist.TxSynchronizable";

    /** Property specifying whether JDBC 3.0-specific features should be used, 
     *  such as e.g. the use of Statement.getGeneratedKeys() 
     *  <pre>org.castor.jdo.use.jdbc30</pre> */
    public static final String USE_JDBC30 =
        "org.castor.jdo.use.jdbc30";
    
    /** Property specifying whether JDBC proxy classes should be used 
     * <pre>org.exolab.castor.persist.useProxies</pre>*/
    public static final String USE_JDBC_PROXIES =
        "org.exolab.castor.persist.useProxies";

    /** Property listing all available {@link org.exolab.castor.mapping.MappingLoader}
     * implementations (<tt>org.castor.mapping.Loaders</tt>). */
    public static final String MAPPING_LOADER_FACTORIES =
        "org.castor.mapping.loaderFactories";

}
