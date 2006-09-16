package org.exolab.javasource;

import java.util.HashMap;

public final class Java5HacksHelper {

    // This is a static class wrapper
    private Java5HacksHelper() { }
    
    private static class MethodSpec {
        public String _methodName = null;
        public int _paramCount = 0;
        public String _param1ClassName = null;
        public String _param2ClassName = null;
    }

    private static final HashMap DEFINED_SPECS = new HashMap();

    static void createMethodSpec(final String methodName, final int paramCount,
            final String param1ClassName, final String param2ClassName) {
        MethodSpec temp = new MethodSpec();
        temp._methodName = methodName;
        temp._paramCount = paramCount;
        temp._param1ClassName = param1ClassName;
        temp._param2ClassName = param2ClassName;
        
        DEFINED_SPECS.put(methodName, temp);
    }

    static {
        createMethodSpec("getAccessMode",       0, "",                                         ""); 
        createMethodSpec("getExtends",          0, "",                                         ""); 
        createMethodSpec("getIdentity",         0, "",                                         ""); 
        createMethodSpec("getJavaClass",        0, "",                                         ""); 
        createMethodSpec("getNameSpacePrefix",  0, "",                                         ""); 
        createMethodSpec("getNameSpaceURI",     0, "",                                         ""); 
        createMethodSpec("getValidator",        0, "",                                         ""); 
        createMethodSpec("getXMLName",          0, "",                                         ""); 
        createMethodSpec("getXTransients",      0, "",                                         ""); 
        createMethodSpec("newInstance",         1, "java.lang.Object",                         ""); 
        createMethodSpec("setValue",            1, "java.lang.Object",                         ""); 
        createMethodSpec("equals",              1, "java.lang.Object",                         ""); 
        createMethodSpec("getValue",            1, "java.lang.Object",                         ""); 
        createMethodSpec("marshal",             1, "java.io.Writer",                           ""); 
        createMethodSpec("newInstance",         1, "java.lang.Object",                         ""); 
        createMethodSpec("setValue",            2, "java.lang.Object",                         "java.lang.Object"); 
        createMethodSpec("setXTransients",      1, "org.openmrm.core.data.castor.XTransients", ""); 
//      createMethodSpec("getAttributeId",      0, "",                                         ""); 
    };

    public static void addOverrideAnnotations(final JMethodSignature jms) {
        String name = jms.getName();
        boolean addOverrideAnnotation = false;
        
        // 0. It the method already has an override annotation, then jump out 
        JAnnotation override = jms.getAnnotation(new JAnnotationType("Override"));
        if (override != null) {
            return;
        }
        
        // 1. Does the method name exist in our list
//        MethodSpec methodSpec = definedSpecs.get(name);
        MethodSpec methodSpec = (MethodSpec) DEFINED_SPECS.get(name);
        
        // 2. If we have a methodSpec, then test the parameters
        if (methodSpec != null) {
            
            // if the correct number of parameters
            int paramCount = jms.getParameters().length;
            if (paramCount == methodSpec._paramCount) {

                
                // then check the 0 param methods
                if ((paramCount == 0) && (methodSpec._paramCount == 0)) {
                    addOverrideAnnotation = true;
                        
                }
                
                
                // then check the 1 param methods
                if ((paramCount == 1) && (methodSpec._paramCount == 1)) {
                    String incommingClassName1 = jms.getParameter(0).getType().getName();
                    if (incommingClassName1.equalsIgnoreCase(
                            methodSpec._param1ClassName)) {
                        addOverrideAnnotation = true;
                    }
                        
                }

                // then check the 2 param methods
                if (paramCount == 2 && methodSpec._paramCount == 2) { 
                    String incommingClassName1 = jms.getParameter(0).getType().getName();
                    String incommingClassName2 = jms.getParameter(1).getType().getName();
                    if (incommingClassName1.equalsIgnoreCase(methodSpec._param1ClassName)
                            && incommingClassName2.equalsIgnoreCase(
                                    methodSpec._param2ClassName)) {
                        addOverrideAnnotation = true;
                    }
                }
            }
        }
        
        // 3. do the work if we need to
        if (addOverrideAnnotation) {
            jms.addAnnotation(new JAnnotation(new JAnnotationType("Override")));
        }
    }
}
