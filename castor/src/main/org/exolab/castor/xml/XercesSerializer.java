/*
 * Copyright 2005 Werner Guttmann
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
package org.exolab.castor.xml;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.util.Messages;
import org.xml.sax.DocumentHandler;

/**
 * Xerces-specific implementation of the Serializer interface.
 * @author Werner Guttmann
 */
public class XercesSerializer implements org.exolab.castor.xml.Serializer {
    
	private static final Log LOG = LogFactory.getLog(XercesSerializer.class);
	
    /**
     * Xerces XMLSerializer instance to use for serialization.
     */
    private Object _serializer;

    /**
     * Creates an instance of this class.
     */
    public XercesSerializer() {

        try {
            _serializer =  
            Class.forName("org.apache.xml.serialize.XMLSerializer").newInstance();
        } catch (Exception except) {
            throw new RuntimeException(
                    Messages.format("conf.failedInstantiateSerializer", 
                            "org.apache.xml.serialize.XMLSerializer", except));
        }
        
    }
    
    /**
     * @inheritDoc
     */
    public void setOutputCharStream(Writer out) {
    	Method method;
		try {
			method = _serializer.getClass().getMethod("setOutputCharStream", new Class[] {Writer.class} );
	    	method.invoke(_serializer, new Object[] { out });
		} catch (Exception e) {
			LOG.error ("Problem invoking XMLSerializer.setOutputCharStream()", e);
			throw new RuntimeException ("Problem invoking XMLSerializer.setOutputCharStream()" + e.getMessage()); 
		}
        // _serializer.setOutputCharStream(out);
    }

    /**
     * @inheritDoc
     */
    public DocumentHandler asDocumentHandler() throws IOException {
    	Method method;
		try {
			method = _serializer.getClass().getMethod("asDocumentHandler", (Class[]) null);
	    	return (DocumentHandler) method.invoke(_serializer, (Object[]) null);
		} catch (Exception e) {
			LOG.error ("Problem invoking XMLSerializer.asDocumentHandler()", e);
			throw new RuntimeException ("Problem invoking XMLSerializer.asDocumentHandler()" + e.getMessage()); 
		}
        // return _serializer.asDocumentHandler();
    }

    /**
     * @inheritDoc
     */
    public void setOutputFormat(org.exolab.castor.xml.OutputFormat format) {
    	Method method;
		try {
			Class outputFormatClass =
				Class.forName("org.apache.xml.serialize.OutputFormat");
			method = _serializer.getClass().getMethod("setOutputFormat", new Class[] {outputFormatClass } );
	    	method.invoke(_serializer, new Object[] { format.getFormat() });
		} catch (Exception e) {
			LOG.error ("Problem invoking XMLSerializer.setOutputFormat()", e);
			throw new RuntimeException ("Problem invoking XMLSerializer.setOutputFormat()" + e.getMessage()); 
		}
        // _serializer.setOutputFormat((OutputFormat) format.getFormat());
    }

    /**
     * @inheritDoc
     */
    public void setOutputByteStream(OutputStream output) {
    	Method method;
		try {
			method = _serializer.getClass().getMethod("setOutputByteStream", new Class[] {OutputStream.class} );
	    	method.invoke(_serializer, new Object[] { output });
		} catch (Exception e) {
			LOG.error ("Problem invoking XMLSerializer.setOutputByteStream()", e);
			throw new RuntimeException ("Problem invoking XMLSerializer.setOutputByteStream()" + e.getMessage()); 
		}
        // _serializer.setOutputByteStream(output);
    }

}
