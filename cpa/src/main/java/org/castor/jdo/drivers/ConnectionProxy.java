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
 */
package org.castor.jdo.drivers;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Proxy class for JDBC Connection class, to allow information gathering
 * for the purpose of SQL statement logging.
 * 
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.0.4
 */
public final class ConnectionProxy implements InvocationHandler {
    /** Jakarta Common Log instance. */
    private static final Log LOG = LogFactory.getLog(ConnectionProxy.class);
    
    /** The JDBC Connection instance to proxy. */
    private final Connection _connection;
    
    /** Name of the class that created this ConnectionProxy instance. */
    private final String _calledBy;
    
    /**
     * Creates an instance of ConnectionProxy.
     * 
     * @param con JDBC Connectio instance to be proxied.
     * @param calledBy Name of the class using creating and this proxy class. 
     */
    protected ConnectionProxy(final Connection con, final String calledBy) {
        _connection = con;
        _calledBy = calledBy;
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Creating ConnectionProxy for calling class " + _calledBy);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Object invoke(final Object proxy, final Method method, final Object[] args)
    throws Throwable {
        String name = method.getName();
        
        if (LOG.isDebugEnabled()) {
            if ("close".equals(name)) {
                LOG.debug("Closing JDBC Connection instance.");
            } else if ("commit".equals(name)) {
                LOG.debug("Committing JDBC Connection instance.");
            } else if ("createStatement".equals(name)) {
                LOG.debug("Creating JDBC Statement for Connection instance.");
            } else if ("rollback".equals(name)) {
                LOG.debug("Rolling back JDBC Connection instance.");
            }
        }
        
        if ("prepareCall".equals(name)) {
            return ConnectionProxyFactory.newCallableStatementProxy(
                    (CallableStatement) method.invoke(_connection, args),
                    (String) args[0]);
        } else if ("prepareStatement".equals(name)) {
            return ConnectionProxyFactory.newPreparedStatementProxy(
                    (PreparedStatement) method.invoke(_connection, args),
                    (String) args[0]);
        } else if ("toString".equals(name)) {
            return getClass().getName() + " created and called by " + _calledBy;
        } else {
            return method.invoke(_connection, args);
        }
    }
}
