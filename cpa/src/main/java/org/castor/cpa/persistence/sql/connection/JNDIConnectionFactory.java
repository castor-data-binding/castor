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
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.util.Messages;
import org.castor.jdo.conf.Jndi;
import org.exolab.castor.mapping.MappingException;

/**
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-12 15:13:08 -0600 (Wed, 12 Apr 2006) $
 * @since 0.9.9
 */
public final class JNDIConnectionFactory implements ConnectionFactory {
    //--------------------------------------------------------------------------

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(JNDIConnectionFactory.class);

    //--------------------------------------------------------------------------
    
    /** JNDI configuration. */
    private final Jndi _jndi;
    
    /** Wrap JDBC connections by proxies? */
    private final boolean _useProxies;

    /** The data source when using a JDBC dataSource. */
    private DataSource _dataSource = null;

    //--------------------------------------------------------------------------

    /**
     * Constructs a new JNDIConnectionFactory with given database and mapping.
     * 
     * @param jndi JNDI configuration.
     * @param useProxies Wrap JDBC connections by proxies?
     */
    public JNDIConnectionFactory(final Jndi jndi, final boolean useProxies) {
        _jndi = jndi;
        _useProxies = useProxies;
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public void initializeFactory() throws MappingException {
        String name = _jndi.getName();

        Object dataSource;
        try {
            Context initialContext = new InitialContext(); 
            dataSource = initialContext.lookup(name);
        } catch (NameNotFoundException e) {
            String msg = Messages.format("jdo.jndiNameNotFound", name);
            LOG.error(msg, e);
            throw new MappingException(msg, e);
        } catch (NamingException e) {
            throw new MappingException(e);
        }
        
        if (!(dataSource instanceof DataSource)) {
            String msg = Messages.format("jdo.jndiNameNotFound", name);
            LOG.error(msg);
            throw new MappingException(msg);
        }
        
        _dataSource = (DataSource) dataSource;

        if (LOG.isDebugEnabled()) {
            LOG.debug("Using DataSource from JNDI ENC: " + name);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Connection createConnection () throws SQLException {
        Connection connection = _dataSource.getConnection();
        if (!_useProxies) { return connection; }
        return ConnectionProxyFactory.newConnectionProxy(connection, getClass().getName());
    }

    //--------------------------------------------------------------------------
}
