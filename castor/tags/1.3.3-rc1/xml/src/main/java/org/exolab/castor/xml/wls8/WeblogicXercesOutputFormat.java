/*
 * Copyright 2006 Thierry Guerin
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
 */package org.exolab.castor.xml.wls8;

import java.lang.reflect.Method;

/**
 * "Weblogic's refactored Xerces"-specific OutputFormat instance. Uses reflection to
 * get the class and methods to avoid having dependencies towards Weblogic when Castor
 * is not used in a Weblogic server.
 * 
 * @author Thierry Guerin
 */
public class WeblogicXercesOutputFormat extends WeblogicXercesImplementation implements org.exolab.castor.xml.OutputFormat {

    private static Class outputFormatClass;
    private static Method setDoctype;
    private static Method setEncoding;
    private static Method setVersion;
    private static Method setIndenting;
    private static Method setMethod;
    private static Method setOmitDocumentType;
    private static Method setOmitXMLDeclaration;
    private static Method setPreserveSpace;
    // the actual Weblogic implementation instance
    private Object outputFormat;
    
    static {
        // use reflection to get the methods

        // get the class
        String wlsOutputFormatClassFqcn = "weblogic.apache.xml.serialize.OutputFormat";
        try {
            outputFormatClass = Class.forName(wlsOutputFormatClassFqcn);
        }
        catch (ClassNotFoundException e) {
            handleStaticInitException("Could find class " + wlsOutputFormatClassFqcn, e);
        }
        // get the methods
        // setDoctype
        setDoctype = getMethod(outputFormatClass, "setDoctype", new Class[] { String.class, String.class });

        // setEncoding
        setEncoding = getMethod(outputFormatClass, "setEncoding", new Class[] { String.class });

        // setVersion
        setEncoding = getMethod(outputFormatClass, "setVersion", new Class[] { String.class });

        // setIndenting
        setIndenting = getMethod(outputFormatClass, "setIndenting", new Class[] { boolean.class });
        
        // setMethod
        setMethod = getMethod(outputFormatClass, "setMethod", new Class[] { String.class });
        
        // setOmitDocumentType
        setOmitDocumentType = getMethod(outputFormatClass, "setOmitDocumentType", new Class[] { boolean.class });
        
        // setOmitXMLDeclaration
        setOmitXMLDeclaration = getMethod(outputFormatClass, "setOmitXMLDeclaration", new Class[] { boolean.class });
        
        // setPreserveSpace
        setPreserveSpace = getMethod(outputFormatClass, "setPreserveSpace", new Class[] { boolean.class });
        
    }

    /**
     * Creates an instance of this class. 
     */
    public WeblogicXercesOutputFormat() {
        try {
            outputFormat = outputFormatClass.newInstance();
        }
        catch (InstantiationException e) {
            throw new RuntimeException(e.toString()); // java 1.3, can't wrap using the 1.4 constructor
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e.toString()); // java 1.3, can't wrap using the 1.4 constructor
        }
    }
    
    public void setMethod(String method) {
        Object[] params = {method};
        invoke(setMethod, params);
    }

    public Object getFormat() {
        return outputFormat;
    }
    
    public void setIndenting(boolean indent) {
        // wrap boolean primitive in a Boolean object because the invoke method does the following:
        // Individual parameters are automatically unwrapped to match primitive formal parameters.
        Boolean[] params = {Boolean.valueOf(indent)};
        invoke(setIndenting, params);
    }

    public void setPreserveSpace(boolean preserveSpace) {
        Boolean[] params = {new Boolean(preserveSpace)};
        invoke(setPreserveSpace, params);
    }

    public void setDoctype (String type1, String type2) {
        Object[] params = {type1, type2};
        invoke(setDoctype, params);
    }

    public void setOmitXMLDeclaration(boolean omitXMLDeclaration) {
        Boolean[] params = {Boolean.valueOf(omitXMLDeclaration)};
        invoke(setOmitXMLDeclaration, params);
    }

    public void setOmitDocumentType(boolean omitDocumentType) {
        Boolean[] params = {Boolean.valueOf(omitDocumentType)};
        invoke(setOmitDocumentType, params);
    }

    public void setEncoding(String encoding) {
        String[] params = {encoding};
        invoke(setEncoding, params);
    }

    public void setVersion(String version) {
        String[] params = {version};
        invoke(setEncoding, params);
    }

    private Object invoke(Method method, Object[] params) {
        return invoke(outputFormat, method, params);
    }
    
}
