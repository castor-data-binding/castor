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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.jdo.conf.DatabaseChoice;
import org.castor.jdo.conf.Driver;
import org.castor.jdo.conf.JdoConf;
import org.castor.jdo.conf.Param;
import org.castor.jdo.drivers.ConnectionProxy;
import org.castor.util.Messages;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;

/**
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-12 15:13:08 -0600 (Wed, 12 Apr 2006) $
 * @since 0.9.9
 */
public final class DriverConnectionFactory extends AbstractConnectionFactory {
    //--------------------------------------------------------------------------

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(DriverConnectionFactory.class);

    //--------------------------------------------------------------------------

    /** The JDBC URL when using a JDBC driver. Only available after initialization. */
    private String            _url = null;

    /** The properties when using a JDBC driver. Only available after initialization. */
    private Properties        _props;

    //--------------------------------------------------------------------------

    /**
     * Constructs a new DriverConnectionFactory with given database and mapping.
     * 
     * @param jdoConf   An in-memory jdo configuration. 
     * @param index     Index of the database configuration inside the jdo configuration.
     * @param mapping   The mapping to load.
     */
    public DriverConnectionFactory(final JdoConf jdoConf, final int index,
                                   final Mapping mapping) {
        super(jdoConf, index, mapping);
    }

    /**
     * {@inheritDoc}
     * @see org.castor.jdo.engine.AbstractConnectionFactory#initializeFactory()
     */
    protected void initializeFactory() throws MappingException {
        Enumeration params;
        Param       param;
        
        DatabaseChoice dbChoice = getDatabase().getDatabaseChoice();
        String driverName = dbChoice.getDriver().getClassName();
        if (driverName != null) {
            try {
                Class.forName(dbChoice.getDriver().getClassName()).newInstance();
            } catch (InstantiationException e) {
                String msg = Messages.format(
                        "jdo.engine.classNotInstantiable", driverName);
                LOG.error(msg, e);
                throw new MappingException(msg, e);
            } catch (IllegalAccessException e) {
                String msg = Messages.format(
                        "jdo.engine.classNotAccessable", driverName, "constructor");
                LOG.error(msg, e);
                throw new MappingException(msg, e);
            } catch (ClassNotFoundException e) {
                String msg = "Can not load class " + driverName;
                LOG.error(msg, e);
                throw new MappingException(msg, e);
            } 
        }
        
        try {
            Driver driver = dbChoice.getDriver();
            if (DriverManager.getDriver(driver.getUrl()) == null) {
                String msg = Messages.format("jdo.missingDriver", driver.getUrl());
                LOG.error(msg);
                throw new MappingException(msg);
            }
        } catch (SQLException ex) {
            throw new MappingException(ex);
        }
        
        _url = dbChoice.getDriver().getUrl();
        
        _props = new Properties();
        params = dbChoice.getDriver().enumerateParam();
        while (params.hasMoreElements()) {
            param = (Param) params.nextElement();
            _props.put(param.getName(), param.getValue());
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Using driver: " + driverName);
        }
    }
    
    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     * @see org.castor.jdo.engine.ConnectionFactory#createConnection()
     */
    public Connection createConnection () throws SQLException {
        return ConnectionProxy.newConnectionProxy(
                DriverManager.getConnection(_url, _props), getClass().getName());
    }

    //--------------------------------------------------------------------------
}
