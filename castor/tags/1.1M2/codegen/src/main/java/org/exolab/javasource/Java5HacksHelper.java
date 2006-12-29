package org.exolab.javasource;

import java.util.HashMap;

/**
 * Makes an effort to identify where Override annotations belong. We are limited
 * in what we look at and we do not use reflection to identify if a class truly
 * extends the appropriate class. We only check the class of the arguments. This
 * class may not be generically useful, but is useful with the code generated by
 * Castor.
 * <p>
 * Our one public method, {@link #addOverrideAnnotations(JMethodSignature)},
 * should only be called when you already know that you are generating Java-5
 * style code.
 */
public final class Java5HacksHelper {

    /**
     * As a static utility class, we want a private constructor.
     */
    private Java5HacksHelper() {
        // Nothing to do
    }

    private static class MethodSpec {
        public String _methodName      = null;
        public int    _paramCount      = 0;
        public String _param1ClassName = null;
        public String _param2ClassName = null;
    }

    private static final HashMap DEFINED_SPECS = new HashMap();

    private static void createMethodSpec(final String methodName, final int paramCount,
            final String param1ClassName, final String param2ClassName) {
        MethodSpec temp = new MethodSpec();
        temp._methodName      = methodName;
        temp._paramCount      = paramCount;
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

    /** An override annotation we use to see if we can get others of its type. */
    private static final JAnnotationType OVERRIDE_ANNOTATION = new JAnnotationType("Override");

    /**
     * Given the method signature, add the Override annotation if this class is
     * one that we know requires this annotation.
     *
     * @param jms
     *            the method signature to inspect
     */
    public static void addOverrideAnnotations(final JMethodSignature jms) {
        String name = jms.getName();
        boolean addOverrideAnnotation = false;

        // It the method already has an override annotation, then jump out
        JAnnotation override = jms.getAnnotation(OVERRIDE_ANNOTATION);
        if (override != null) {
            return;
        }

        // If the method name doesn't exist in our list, then jump out
        MethodSpec methodSpec = (MethodSpec) DEFINED_SPECS.get(name);
        if (methodSpec == null) {
            return;
        }

        // If the number of parameters isn't what we're prepared for, then jump out
        int paramCount = jms.getParameters().length;
        if (paramCount != methodSpec._paramCount) {
            return;
        }

        // Do we add the Override annotation?  Check vs number of arguments.
        switch (paramCount) {
            case 0:
                addOverrideAnnotation = true;
                break;
            case 1:
                String incomingClassName1 = jms.getParameter(0).getType().getName();
                if (incomingClassName1.equalsIgnoreCase(methodSpec._param1ClassName)) {
                    addOverrideAnnotation = true;
                }
                break;
            case 2:
                String className1 = jms.getParameter(0).getType().getName();
                String className2 = jms.getParameter(1).getType().getName();
                if (className1.equalsIgnoreCase(methodSpec._param1ClassName)
                    && className2.equalsIgnoreCase(methodSpec._param2ClassName)) {
                    addOverrideAnnotation = true;
                }
                break;
            default:
                // We aren't prepared for > 2 parameters, so we don't add an Override annotation
                break;
        }

        // Do the work if we need to
        if (addOverrideAnnotation) {
            jms.addAnnotation(new JAnnotation(new JAnnotationType("Override")));
        }
    }

}
