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
package org.castor.cpa.persistence.sql.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.util.Messages;
import org.castor.cpa.persistence.sql.engine.CastorConnection;
import org.castor.jdo.conf.Driver;
import org.castor.jdo.conf.Param;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.persist.spi.PersistenceFactory;

/**
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-12 15:13:08 -0600 (Wed, 12 Apr 2006) $
 * @since 0.9.9
 */
public final class DriverConnectionFactory implements ConnectionFactory {
    //--------------------------------------------------------------------------

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(DriverConnectionFactory.class);

    /** PersistenceFactory to be used to construct CastorConnection. */
    private PersistenceFactory _factory;

    //--------------------------------------------------------------------------

    /** Driver configuration. */
    private final Driver _driver;

    /** Wrap JDBC connections by proxies? */
    private final boolean _useProxies;
    
    /** The JDBC URL when using a JDBC driver. Only available after initialization. */
    private String _url = null;

    /** The properties when using a JDBC driver. Only available after initialization. */
    private Properties _props = null;

    //--------------------------------------------------------------------------

    /**
     * Constructs a new DriverConnectionFactory with given database and mapping.
     * 
     * @param driver Driver configuration.
     * @param useProxies Wrap JDBC connections by proxies?
     */
    public DriverConnectionFactory(final Driver driver, final boolean useProxies) {
        _driver = driver;
        _useProxies = useProxies;
    }

    /**
     * {@inheritDoc}
     */
    public void initializeFactory(final PersistenceFactory factory) throws MappingException {
        _factory = factory;
        String driverName = _driver.getClassName();
        if (driverName != null) {
            try {
                Class.forName(driverName).newInstance();
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
        
        _url = _driver.getUrl();
        try {
            if (DriverManager.getDriver(_url) == null) {
                String msg = Messages.format("jdo.missingDriver", _url);
                LOG.error(msg);
                throw new MappingException(msg);
            }
        } catch (SQLException ex) {
            throw new MappingException(ex);
        }
        
        _props = new Properties();
        Enumeration<? extends Param> params = _driver.enumerateParam();
        while (params.hasMoreElements()) {
            Param param = params.nextElement();
            _props.put(param.getName(), param.getValue());
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Using driver: " + driverName);
        }
    }
    
    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public Connection createConnection () throws SQLException {
        Connection connection = DriverManager.getConnection(_url, _props);
        if (!_useProxies) { return connection; }
        return ConnectionProxyFactory.newConnectionProxy(connection, getClass().getName());
    }

    /**
     * {@inheritDoc}
     */
    public CastorConnection createCastorConnection () throws SQLException {
        return new CastorConnection(_factory, createConnection());
    }

    //--------------------------------------------------------------------------
}
