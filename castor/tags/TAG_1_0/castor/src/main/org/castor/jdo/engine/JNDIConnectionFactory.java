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
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.jdo.conf.DatabaseChoice;
import org.castor.jdo.conf.JdoConf;
import org.castor.jdo.drivers.ConnectionProxy;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.util.Messages;

/**
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 0.9.9
 */
public final class JNDIConnectionFactory extends AbstractConnectionFactory {
    //--------------------------------------------------------------------------

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(JNDIConnectionFactory.class);

    //--------------------------------------------------------------------------

    /** The data source when using a JDBC dataSource. */
    private DataSource        _dataSource = null;

    //--------------------------------------------------------------------------

    /**
     * Constructs a new JNDIConnectionFactory with given database and mapping.
     * 
     * @param jdoConf   An in-memory jdo configuration. 
     * @param index     Index of the database configuration inside the jdo configuration.
     * @param mapping   The mapping to load.
     */
    public JNDIConnectionFactory(final JdoConf jdoConf, final int index,
                                 final Mapping mapping) {
        super(jdoConf, index, mapping);
    }

    /**
     * @see org.castor.jdo.engine.AbstractConnectionFactory#initializeFactory()
     */
    protected void initializeFactory() throws MappingException {
        Object dataSource;
        
        DatabaseChoice dbChoice = getDatabase().getDatabaseChoice();
        String name = null;

        try {
            name = dbChoice.getJndi().getName();
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
