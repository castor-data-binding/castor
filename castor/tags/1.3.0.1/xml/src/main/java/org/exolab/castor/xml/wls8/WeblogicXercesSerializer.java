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
 */
package org.exolab.castor.xml.wls8;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.reflect.Method;

/**
 * Xerces-specific implementation of the Serializer interface. Uses reflection to get
 * the class and methods to avoid having dependencies towards Weblogic when Castor is
 * not used in a Weblogic server.
 * 
 * @author Thierry Guerin
 */
public class WeblogicXercesSerializer extends WeblogicXercesImplementation implements org.exolab.castor.xml.Serializer {
    
    private static Class serializerClass;
    private static Method asDocumentHandler;
    private static Method setOutputByteStream;
    private static Method setOutputCharStream;
    private static Method setOutputFormat;

    /**
     * Xerces XMLSerializer instance to use for serialization.
     */
    private Object serializer;
    static {
        // use reflection to get the methods

        // get the classes
        Class weblogicOutputFormat = null;
        try {
            serializerClass = Class.forName("weblogic.apache.xml.serialize.XMLSerializer");
            weblogicOutputFormat = Class.forName("weblogic.apache.xml.serialize.OutputFormat");
        }
        catch (ClassNotFoundException e) {
            handleStaticInitException(e);
        }
        // get the methods
        // asDocumentHandler
        asDocumentHandler = getMethod(serializerClass, "asDocumentHandler", new Class[0]);
        
        // setOutputByteStream
        Class[] parameterOutputStream = {OutputStream.class};
        setOutputByteStream = getMethod(serializerClass, "setOutputByteStream", parameterOutputStream);
        
        // setOutputCharStream
        Class[] parameterWriter = {Writer.class};
        setOutputCharStream = getMethod(serializerClass, "setOutputCharStream", parameterWriter);
        
        // setOutputByteStream
        Class[] parameterOutputFormat = {weblogicOutputFormat};
        setOutputFormat = getMethod(serializerClass, "setOutputFormat", parameterOutputFormat);
        
    }

    /**
     * Creates an instance of this class.
     */
    public WeblogicXercesSerializer() {
        try {
            serializer = serializerClass.newInstance();
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
     * @deprecated
     */
    public org.xml.sax.DocumentHandler asDocumentHandler() throws IOException {
        return (org.xml.sax.DocumentHandler) invoke(asDocumentHandler, new Object[0]);
    }
    
    /**
     * @inheritDoc
     */
    public void setOutputByteStream(OutputStream output) {
        Object[] params = {output};
        invoke(setOutputByteStream, params);
    }

    /**
     * @inheritDoc
     */
    public void setOutputCharStream(Writer out) {
        Object[] params = {out};
        invoke(setOutputCharStream, params);
    }
    
    /**
     * @inheritDoc
     */
    public void setOutputFormat(org.exolab.castor.xml.OutputFormat format) {
        Object[] params = {format.getFormat()};
        invoke(setOutputFormat, params);
    }
    
    private Object invoke(Method method, Object[] params) {
        return invoke(serializer, method, params);
    }
}
