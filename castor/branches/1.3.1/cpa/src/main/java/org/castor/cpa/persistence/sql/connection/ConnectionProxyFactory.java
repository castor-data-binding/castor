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
package org.castor.cpa.persistence.sql.connection;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Factory class for proxies for JDBC Connection, PreparedStatement and CallableStatement
 * classes. The proxies allow to gather information for the purpose of SQL statement
 * logging.
 * 
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date$
 * @since 1.0.4
 */
public final class ConnectionProxyFactory {
    //--------------------------------------------------------------------------

    /**
     * Factory method for creating a ConnectionProxy.
     * 
     * @param connection The JDBC connection to proxy.
     * @param calledBy Name of the class using creating and this proxy class. 
     * @return The JDBC connection proxy.
     */
    public static Connection newConnectionProxy(
            final Connection connection, final String calledBy) {
        ClassLoader loader = connection.getClass().getClassLoader();
        Class<?>[] interfaces = new Class<?>[] {Connection.class};
        InvocationHandler handler = new ConnectionProxy(connection, calledBy);
        return (Connection) Proxy.newProxyInstance(loader, interfaces, handler);
    }

    /**
     * Factory method for creating a PreparedStamentProxy.
     * 
     * @param statement Prepared statement to be proxied.
     * @param sql SQL string.
     * @return Prepared statement proxy.
     */
    protected static PreparedStatement newPreparedStatementProxy(
            final PreparedStatement statement, final String sql) {
        ClassLoader loader = statement.getClass().getClassLoader();
        Class<?>[] interfaces = new Class<?>[] {PreparedStatement.class};
        InvocationHandler handler = new PreparedStatementProxy(statement, sql);
        return (PreparedStatement) Proxy.newProxyInstance(loader, interfaces, handler);
    }
    
    /**
     * Factory method for creating a CallableStamentProxy.
     * 
     * @param statement Callable statement to be proxied.
     * @param sql SQL string.
     * @return Callable statement proxy.
     */
    protected static CallableStatement newCallableStatementProxy(
            final CallableStatement statement, final String sql) {
        ClassLoader loader = statement.getClass().getClassLoader();
        Class<?>[] interfaces = new Class<?>[] {CallableStatement.class};
        InvocationHandler handler = new CallableStatementProxy(statement, sql);
        return (CallableStatement) Proxy.newProxyInstance(loader, interfaces, handler);
    }
    
    //--------------------------------------------------------------------------

    /**
     * Hide utility class constructor.
     */
    private ConnectionProxyFactory() { }

    //--------------------------------------------------------------------------
}
