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
    
    private static Class _serializerClass;
    private static Method _asDocumentHandler;
    private static Method _setOutputByteStream;
    private static Method _setOutputCharStream;
    private static Method _setOutputFormat;

    /** Xerces XMLSerializer instance to use for serialization. */
    private Object _serializer;
    static {
        // use reflection to get the methods

        // get the classes
        Class weblogicOutputFormat = null;
        try {
            _serializerClass = Class.forName("weblogic.apache.xml.serialize.XMLSerializer");
            weblogicOutputFormat = Class.forName("weblogic.apache.xml.serialize.OutputFormat");
        }
        catch (ClassNotFoundException e) {
            handleStaticInitException(e);
        }
        // get the methods
        // asDocumentHandler
        _asDocumentHandler = getMethod(_serializerClass, "asDocumentHandler", new Class[0]);
        
        // setOutputByteStream
        Class[] parameterOutputStream = {OutputStream.class};
        _setOutputByteStream = getMethod(_serializerClass, "setOutputByteStream", parameterOutputStream);
        
        // setOutputCharStream
        Class[] parameterWriter = {Writer.class};
        _setOutputCharStream = getMethod(_serializerClass, "setOutputCharStream", parameterWriter);
        
        // setOutputByteStream
        Class[] parameterOutputFormat = {weblogicOutputFormat};
        _setOutputFormat = getMethod(_serializerClass, "setOutputFormat", parameterOutputFormat);
        
    }

    /**
     * Creates an instance of this class.
     */
    public WeblogicXercesSerializer() {
        try {
            _serializer = _serializerClass.newInstance();
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
        return (org.xml.sax.DocumentHandler) invoke(_asDocumentHandler, new Object[0]);
    }
    
    /**
     * @inheritDoc
     */
    public void setOutputByteStream(OutputStream output) {
        Object[] params = {output};
        invoke(_setOutputByteStream, params);
    }

    /**
     * @inheritDoc
     */
    public void setOutputCharStream(Writer out) {
        Object[] params = {out};
        invoke(_setOutputCharStream, params);
    }
    
    /**
     * @inheritDoc
     */
    public void setOutputFormat(org.exolab.castor.xml.OutputFormat format) {
        Object[] params = {format.getFormat()};
        invoke(_setOutputFormat, params);
    }
    
    private Object invoke(Method method, Object[] params) {
        return invoke(_serializer, method, params);
    }
}
