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
        Class[] parameterTwoStrings = {String.class, String.class};
        setDoctype = getMethod(outputFormatClass, "setDoctype", parameterTwoStrings);

        // setEncoding
        Class[] parameterOneString = {String.class};
        setEncoding = getMethod(outputFormatClass, "setEncoding", parameterOneString);
        
        // setIndenting
        Class[] parameterBoolean = {boolean.class};
        setIndenting = getMethod(outputFormatClass, "setIndenting", parameterBoolean);
        
        // setMethod
        setMethod = getMethod(outputFormatClass, "setMethod", parameterOneString);
        
        // setOmitDocumentType
        setOmitDocumentType = getMethod(outputFormatClass, "setOmitDocumentType", parameterBoolean);
        
        // setOmitXMLDeclaration
        setOmitXMLDeclaration = getMethod(outputFormatClass, "setOmitXMLDeclaration", parameterBoolean);
        
        // setPreserveSpace
        setPreserveSpace = getMethod(outputFormatClass, "setPreserveSpace", parameterBoolean);
        
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
    
    /**
     * @inheritDoc
     */
    public void setMethod(String method) {
        Object[] params = {method};
        invoke(setMethod, params);
    }

    /**
     * @inheritDoc
     */
    public void setIndenting(boolean indent) {
        // wrap boolean primitive in a Boolean object because the invoke method does the following:
        // Individual parameters are automatically unwrapped to match primitive formal parameters.
        Boolean[] params = {Boolean.valueOf(indent)};
        invoke(setIndenting, params);
    }

    /**
     * @inheritDoc
     */
    public void setPreserveSpace(boolean preserveSpace) {
        Boolean[] params = {new Boolean(preserveSpace)};
        invoke(setPreserveSpace, params);
    }

    /**
     * @inheritDoc
     */
    public Object getFormat() {
        return outputFormat;
    }
    
    /**
     * @inheritDoc
     */
    public void setDoctype (String type1, String type2) {
        Object[] params = {type1, type2};
        invoke(setDoctype, params);
    }

    /**
     * @inheritDoc
     */
    public void setOmitXMLDeclaration(boolean omitXMLDeclaration) {
        Boolean[] params = {Boolean.valueOf(omitXMLDeclaration)};
        invoke(setOmitXMLDeclaration, params);
    }

    /**
     * @inheritDoc
     */
    public void setOmitDocumentType(boolean omitDocumentType) {
        Boolean[] params = {Boolean.valueOf(omitDocumentType)};
        invoke(setOmitDocumentType, params);
    }

    /**
     * @inheritDoc
     */
    public void setEncoding(String encoding) {
        String[] params = {encoding};
        invoke(setEncoding, params);
    }

    private Object invoke(Method method, Object[] params) {
        return invoke(outputFormat, method, params);
    }
}
