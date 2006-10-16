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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Proxy class for JDBC CallableStatement class, to allow information gathering
 * for the purpose of SQL statement logging.
 * 
 * @author <a href="ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.0.4
 */
public final class CallableStatementProxy implements InvocationHandler {
    /** Jakarta Common Log instance. */
    private static final Log LOG = LogFactory.getLog(CallableStatementProxy.class);
    
    /** Set of setter methods that all have to be treated similare at invoke. */
    private static final Set SET_METHODS = new HashSet(Arrays.asList(new String[] {
            "setArray", "setBigDecimal", "setBinaryStream", "setBlob", "setBoolean",
            "setByte", "setBytes", "setCharacterStream", "setClob", "setDate",
            "setDouble", "setFloat", "setInt", "setLong", "setObject", "setShort",
            "setString", "setTime", "setTimestamp", "setURL"}));

    /** CallableStatement to be proxied. */
    private final CallableStatement _callableStatement;

    /** The SQL statement to be executed.  */
    private String _sqlStatement;

    /** SQL Parameter mapping. */
    private final Map _parameters = new HashMap();

    /** List of batch statements associated with this instance. */
    private final List _batchStatements = new ArrayList();

    /**
     * Creates an instance of this class.
     * 
     * @param stmt Callable statement to be proxied.
     * @param sql SQL string.
     */
    protected CallableStatementProxy(final CallableStatement stmt, final String sql) {
        _callableStatement = stmt;
        _sqlStatement = sql;
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Creating callable statement proxy for SQL statement " + sql);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Object invoke(final Object proxy, final Method method, final Object[] args)
    throws Throwable {
        String name = method.getName();
        
        if ("clearBatch".equals(name)) {
            _batchStatements.clear();
        } else if ("clearParameters".equals(name)) {
            _parameters.clear();
        } else if ((args != null) && (args.length > 0)) {
            if ("execute".equals(name)
                    || "executeQuery".equals(name)
                    || "executeUpdate".equals(name)) {
                
                _sqlStatement = (String) args[0];
            } else if ("addBatch".equals(name)) {
                _batchStatements.add(args[0]);
            } else if (args[0] instanceof Integer) {
                if ("setNull".equals(name)) {
                    _parameters.put(args[0], "null");
                } else if (SET_METHODS.contains(name)) {
                    _parameters.put(args[0], args[1]);
                }
            }
        } else if ("toString".equals(name)) {
            StringBuffer buffer = new StringBuffer();
            
            StringTokenizer tokenizer = new StringTokenizer(_sqlStatement, "?");
            Iterator iter = new TreeSet(_parameters.keySet()).iterator();
            while (tokenizer.hasMoreTokens()) {
                String partOfStatement = tokenizer.nextToken();
                if (iter.hasNext()) {
                    buffer.append(partOfStatement);
                    buffer.append("'");
                    buffer.append(_parameters.get(iter.next()).toString());
                    buffer.append("'");
                } else {
                    buffer.append(partOfStatement);
                    buffer.append("?");
                }
            }
            
            return buffer.toString();
        }

        return method.invoke(_callableStatement, args);
    }
}
