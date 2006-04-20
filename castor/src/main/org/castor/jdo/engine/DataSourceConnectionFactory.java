/*
 * Copyright 2005 Werner Guttmann, Ralf Joachim
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
package org.castor.jdo.engine;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;
import javax.transaction.TransactionManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.exolab.castor.jdo.conf.Database;
import org.exolab.castor.jdo.conf.DatabaseChoice;
import org.exolab.castor.jdo.conf.JdoConf;
import org.exolab.castor.jdo.conf.Param;
import org.exolab.castor.jdo.drivers.ConnectionProxy;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.util.Messages;

/**
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 0.9.9
 */
public final class DataSourceConnectionFactory extends AbstractConnectionFactory {
    //--------------------------------------------------------------------------

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(DataSourceConnectionFactory.class);

    //--------------------------------------------------------------------------

    /**
     * Initialize JDBC DataSource instance with the given database configuration
     * instances and the given class loader.
     * 
     * @param  database     Database configuration.
     * @param  loader       ClassLoader to use. 
     * @return The initalized DataSource.
     * @throws MappingException Problem related to analysing the JDO configuration.
     */
    public static DataSource loadDataSource(final Database database,
                                            final ClassLoader loader) 
    throws MappingException {
        DataSource dataSource;
        Param[] parameters;
        Param param;

        DatabaseChoice dbChoice = database.getDatabaseChoice();
        String className = dbChoice.getDataSource().getClassName();
        ClassLoader classLoader = loader;
        if (classLoader == null) {
            classLoader = Thread.currentThread().getContextClassLoader();
        }

        try {
            Class dsClass = Class.forName(className, true, classLoader);
            dataSource = (DataSource) dsClass.newInstance();
        } catch (Exception e) {
            String msg = Messages.format("jdo.engine.classNotInstantiable", className);
            LOG.error(msg, e);
            throw new MappingException(msg, e);
        }
        
        parameters = database.getDatabaseChoice().getDataSource().getParam();
        setParameters(dataSource, parameters);
        
        return dataSource;
    }
    
    /**
     * Set all the parameters of the given array at the given datasource by calling
     * one of the set methods of the datasource.
     * 
     * @param dataSource The datasource to set the parameters on.
     * @param params The parameters to set on the datasource.
     * @throws MappingException If one of the parameters could not be set.
     */
    public static void setParameters(final DataSource dataSource, final Param[] params)
    throws MappingException {
        Method[] methods = dataSource.getClass().getMethods();
        
        for (int j = 0; j < params.length; j++) {
            String name = buildMethodName(params[j].getName());
            String value = params[j].getValue();

            boolean success = false;
            Exception cause = null;
            
            try {
                int i = 0;
                while (!success && (i < methods.length)) {
                    Method method = methods[i];
                    Class[] types = method.getParameterTypes();
                    if ((method.getName().equals(name)) && (types.length == 1)) {
                        if (types[0] == String.class) {
                            method.invoke(dataSource, new Object[] {value});
                            success = true;
                        } else if (types[0] == int.class) {
                            method.invoke(dataSource, new Object[] {new Integer(value)});
                            success = true;
                        } else if (types[0] == boolean.class) {
                            method.invoke(dataSource, new Object[] {new Boolean(value)});
                            success = true;
                        }
                    }
                    i++;
                }
            } catch (Exception e) {
                cause = e;
            }
            
            if (!success || (cause != null)) {
                String msg = Messages.format("jdo.engine.datasourceParaFail",
                                             params[j].getName(), value);
                LOG.error(msg, cause);
                throw new MappingException(msg, cause);
            }
        }
    }
    
    /**
     * Build the name of the method to set the parameter value of the given name. The
     * name of the method is build by preceding given parameter name with 'set' followed
     * by all letters of the name. In addition the first letter and all letters
     * following a '-' sign are converted to upper case. 
     * 
     * @param name The name of the parameter.
     * @return The name of the method to set the value of this parameter.
     */
    public static String buildMethodName(final String name) {
        StringBuffer sb = new StringBuffer("set");
        boolean first = true;
        for (int i = 0; i < name.length(); i++) {
            char chr = name.charAt(i);
            if (first && Character.isLowerCase(chr)) {
                sb.append(Character.toUpperCase(chr));
                first = false;
            } else if (Character.isLetter(chr)) {
                sb.append(chr);
                first = false;
            } else if (chr == '-') {
                first = true;
            }
        }
        return sb.toString();
    }

    //--------------------------------------------------------------------------

    /** The data source when using a JDBC dataSource. */
    private DataSource        _dataSource = null;

    //--------------------------------------------------------------------------

    /**
     * Constructs a new DataSourceConnectionFactory with given name, engine, mapping
     * and datasource. Factory will be ready to use without calling initialize first.
     * 
     * @param name       The Name of the database configuration.
     * @param engine     The Name of the persistence factory to use.
     * @param datasource The preconfigured datasource to use for creating connections.
     * @param mapping    The previously loaded mapping.
     * @param txManager  The transaction manager to use.
     * @throws MappingException If LockEngine could not be initialized.
     */
    public DataSourceConnectionFactory(
            final String name, final String engine, final DataSource datasource,
            final Mapping mapping, final TransactionManager txManager)
    throws MappingException {
        super(name, engine, mapping, txManager);
        
        _dataSource = datasource;
    }

    /**
     * Constructs a new DataSourceConnectionFactory with given database and mapping.
     * Initialize needs to be called before using the factory to create connections.
     * 
     * @param jdoConf   An in-memory jdo configuration. 
     * @param index     Index of the database configuration inside the jdo configuration.
     * @param mapping   The mapping to load.
     */
    public DataSourceConnectionFactory(final JdoConf jdoConf, final int index,
                                       final Mapping mapping) {
        super(jdoConf, index, mapping);
    }

    /**
     * @see org.castor.jdo.engine.AbstractConnectionFactory#initializeFactory()
     */
    protected void initializeFactory() throws MappingException {
        _dataSource = loadDataSource(getDatabase(), getMapping().getClassLoader());

        if (LOG.isDebugEnabled()) {
            DatabaseChoice dbc = getDatabase().getDatabaseChoice();
            LOG.debug("Using DataSource: " + dbc.getDataSource().getClassName());
        }
    }

    //--------------------------------------------------------------------------

    /**
     * @see org.castor.jdo.engine.ConnectionFactory#createConnection()
     */
    public Connection createConnection () throws SQLException {
        return ConnectionProxy.newConnectionProxy(
                _dataSource.getConnection(), getClass().getName());
    }

    //--------------------------------------------------------------------------
}
