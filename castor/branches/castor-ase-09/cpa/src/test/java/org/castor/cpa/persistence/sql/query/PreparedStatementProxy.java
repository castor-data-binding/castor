package org.castor.cpa.persistence.sql.query;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.junit.Ignore;

/**
 * Proxy for PreparedStatementMock object to test QueryContext.
 * <br/>
 * We need to implement this mock with an invocation handler as the implemented
 * interface java.sql.PreparedStatement has changed from Java 5 to Java 6. With
 * this change not only new methods have been added but also new classes has been
 * introduced which are referenced by PreparedStatement.
 * 
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
@Ignore
public final class PreparedStatementProxy implements InvocationHandler {
    //--------------------------------------------------------------------------

    protected static PreparedStatementMock createPreparedStatementProxy() {
        ClassLoader loader = PreparedStatementMock.class.getClassLoader();
        Class<?>[] interfaces = new Class<?>[] {PreparedStatementMock.class};
        InvocationHandler handler = new PreparedStatementProxy();
        return (PreparedStatementMock) Proxy.newProxyInstance(loader, interfaces, handler);
    }
    
    //--------------------------------------------------------------------------

    /** Last index passed to call of setNull(int, int). */
    private int _lastIndex = 0;

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public Object invoke(final Object proxy, final Method method, final Object[] args)
    throws Throwable {
        String name = method.getName();
        
        if ("getLastParameterIndex".equals(name)) {
            return _lastIndex;
        } else if ("setNull".equals(name)) {
            _lastIndex = (Integer) args[0];
            return null;
        } else {
            return null;
        }
    }

    //--------------------------------------------------------------------------
}
