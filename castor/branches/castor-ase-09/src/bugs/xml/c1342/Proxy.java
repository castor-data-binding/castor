package xml.c1342;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class Proxy implements MethodInterceptor {
    public static  Object newInstance(final Object object){
        try{
            Proxy proxy = new Proxy(object);
            return Enhancer.create(object.getClass(), new Class[] {}, proxy);
        } catch (Throwable e){
            e.printStackTrace();
            throw new Error(e.getMessage());
        }
    }
    
    private final Object _object;
    
    private Proxy(final Object object) {
        _object = object;
    }
    
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        return method.invoke(_object, args);
    }
}
