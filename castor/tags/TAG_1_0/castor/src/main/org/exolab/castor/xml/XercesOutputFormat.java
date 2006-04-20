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

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.util.Messages;

/**
 * Xerces-specific OutputFormat instance.
 * @author Werner Guttmann
 */
public class XercesOutputFormat implements org.exolab.castor.xml.OutputFormat {

	private static final Log LOG = LogFactory.getLog(XercesSerializer.class);

    private Object _outputFormat;
    
    /**
     * Creates an instance of this class. 
     */
    public XercesOutputFormat() {
        try {
            _outputFormat =  
            Class.forName("org.apache.xml.serialize.OutputFormat").newInstance();
        } catch (Exception except) {
            throw new RuntimeException(
                    Messages.format("conf.failedInstantiateOutputFormat", 
                            "org.apache.xml.serialize.XMLSerializer", except));
        }
        
    }
    
    /**
     * @inheritDoc
     */
    public void setMethod(String method) {
    	Method aMethod;
		try {
			aMethod = _outputFormat.getClass().getMethod("setMethod", new Class[] {String.class} );
	    	aMethod.invoke(_outputFormat, new Object[] { method });
		} catch (Exception e) {
			LOG.error ("Problem invoking OutputFormat.setMethod()", e);
			throw new RuntimeException ("Problem invoking OutputFormat.setMethod():" + e.getMessage()); 
		}
        // _outputFormat.setMethod(method);
    }

    /**
     * @inheritDoc
     */
    public void setIndenting(boolean indent) {
    	Method method;
		try {
			method = _outputFormat.getClass().getMethod("setIndenting", new Class[] {boolean.class} );
	    	method.invoke(_outputFormat, new Object[] { new Boolean(indent) });
		} catch (Exception e) {
			LOG.error ("Problem invoking OutputFormat.setIndenting()", e);
			throw new RuntimeException ("Problem invoking OutputFormat.setIndenting():" + e.getMessage()); 
		}
        // _outputFormat.setIndenting(indent);
    }

    /**
     * @inheritDoc
     */
    public void setPreserveSpace(boolean preserveSpace) {
    	Method method;
		try {
			method = _outputFormat.getClass().getMethod("setPreserveSpace", new Class[] {boolean.class} );
	    	method.invoke(_outputFormat, new Object[] { new Boolean(preserveSpace) });
		} catch (Exception e) {
			LOG.error ("Problem invoking OutputFormat.setPreserveSpace()", e);
			throw new RuntimeException ("Problem invoking OutputFormat.setPreserveSpace():" + e.getMessage()); 
		}
        // _outputFormat.setPreserveSpace(preserveSpace);
    }

    /**
     * @inheritDoc
     */
    public Object getFormat() {
        return _outputFormat;
    }
    
    /**
     * @inheritDoc
     */
    public void setDoctype (String type1, String type2) {
    	Method method;
		try {
			method = _outputFormat.getClass().getMethod("setDoctype", new Class[] {String.class, String.class} );
	    	method.invoke(_outputFormat, new Object[] { type1, type2});
		} catch (Exception e) {
			LOG.error ("Problem invoking OutputFormat.setDoctype()", e);
			throw new RuntimeException ("Problem invoking OutputFormat.setDocytype():" + e.getMessage()); 
		}
        // _outputFormat.setDoctype(type1, type2);
    }

    /**
     * @inheritDoc
     */
    public void setOmitXMLDeclaration(boolean omitXMLDeclaration) {
    	Method method;
		try {
			method = _outputFormat.getClass().getMethod("setOmitXMLDeclaration", new Class[] {boolean.class} );
	    	method.invoke(_outputFormat, new Object[] { new Boolean(omitXMLDeclaration) });
		} catch (Exception e) {
			LOG.error ("Problem invoking OutputFormat.setOmitXMLDeclaration()", e);
			throw new RuntimeException ("Problem invoking OutputFormat.setOmitXMLDeclaration():" + e.getMessage()); 
		}
        // _outputFormat.setOmitXMLDeclaration(omitXMLDeclaration);
    }

    /**
     * @inheritDoc
     */
    public void setOmitDocumentType(boolean omitDocumentType) {
    	Method method;
		try {
			method = _outputFormat.getClass().getMethod("setOmitDocumentType", new Class[] {boolean.class} );
	    	method.invoke(_outputFormat, new Object[] { new Boolean(omitDocumentType) });
		} catch (Exception e) {
			LOG.error ("Problem invoking OutputFormat.setOmitDocumentType()", e);
			throw new RuntimeException ("Problem invoking OutputFormat.setOmitDocumentType():" + e.getMessage()); 
		}
        // _outputFormat.setOmitDocumentType(omitDocumentType);
    }

    /**
     * @inheritDoc
     */
    public void setEncoding(String encoding) {
    	Method method;
		try {
			method = _outputFormat.getClass().getMethod("setEncoding", new Class[] {String.class} );
	    	method.invoke(_outputFormat, new Object[] { encoding });
		} catch (Exception e) {
			LOG.error ("Problem invoking OutputFormat.setEncoding()", e);
			throw new RuntimeException ("Problem invoking OutputFormat.setEncoding():" + e.getMessage()); 
		}
        // _outputFormat.setEncoding(encoding);
    }

}
