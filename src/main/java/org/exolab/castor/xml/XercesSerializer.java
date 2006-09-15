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
import org.castor.util.Messages;
import org.xml.sax.DocumentHandler;

/**
 * Xerces-specific implementation of the Serializer interface.
 * 
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @version $Revision$ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
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
     * @see org.exolab.castor.xml.Serializer#setOutputCharStream(java.io.Writer)
     * {@inheritDoc}
     */
    public void setOutputCharStream(final Writer out) {
    	Method method;
		try {
			method = _serializer.getClass().getMethod(
                    "setOutputCharStream", new Class[] {Writer.class} );
	    	method.invoke(_serializer, new Object[] { out });
		} catch (Exception e) {
            String msg = "Problem invoking XMLSerializer.setOutputCharStream()";
			LOG.error(msg, e);
			throw new RuntimeException(msg + e.getMessage()); 
		}
        // _serializer.setOutputCharStream(out);
    }

    /**
     * @see org.exolab.castor.xml.Serializer#asDocumentHandler()
     * {@inheritDoc}
     */
    public DocumentHandler asDocumentHandler() throws IOException {
    	Method method;
		try {
			method = _serializer.getClass().getMethod(
                    "asDocumentHandler", (Class[]) null);
	    	return (DocumentHandler) method.invoke(_serializer, (Object[]) null);
		} catch (Exception e) {
            String msg = "Problem invoking XMLSerializer.asDocumentHandler()";
			LOG.error(msg, e);
			throw new RuntimeException(msg + e.getMessage()); 
		}
        // return _serializer.asDocumentHandler();
    }

    /**
     * @see org.exolab.castor.xml.Serializer
     *      #setOutputFormat(org.exolab.castor.xml.OutputFormat)
     * {@inheritDoc}
     */
    public void setOutputFormat(final OutputFormat format) {
    	Method method;
		try {
			Class outputFormatClass =
				Class.forName("org.apache.xml.serialize.OutputFormat");
			method = _serializer.getClass().getMethod(
                    "setOutputFormat", new Class[] {outputFormatClass } );
	    	method.invoke(_serializer, new Object[] { format.getFormat() });
		} catch (Exception e) {
            String msg = "Problem invoking XMLSerializer.setOutputFormat()";
			LOG.error(msg, e);
			throw new RuntimeException(msg + e.getMessage()); 
		}
        // _serializer.setOutputFormat((OutputFormat) format.getFormat());
    }

    /**
     * @see org.exolab.castor.xml.Serializer#setOutputByteStream(java.io.OutputStream)
     * {@inheritDoc}
     */
    public void setOutputByteStream(final OutputStream output) {
    	Method method;
		try {
			method = _serializer.getClass().getMethod(
                    "setOutputByteStream", new Class[] {OutputStream.class} );
	    	method.invoke(_serializer, new Object[] { output });
		} catch (Exception e) {
            String msg = "Problem invoking XMLSerializer.setOutputByteStream()";
			LOG.error(msg, e);
			throw new RuntimeException(msg + e.getMessage()); 
		}
        // _serializer.setOutputByteStream(output);
    }
}
