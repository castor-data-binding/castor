/*
 * Copyright 2006 Werner Guttmann
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
import org.castor.core.util.Messages;

/**
 * Xerces-specific OutputFormat instance, used with JDK 5.0 only.
 * 
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @version $Revision: 6216 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 */
public class XercesJDK5OutputFormat implements OutputFormat {
    
    private static final String PACKAGE_NAME = "com.sun.org.apache.xml.internal.serialize"; 
    
	 /**
	 * Logger instance used for logging
	 */
	private static final Log LOG = LogFactory.getLog(XercesSerializer.class);

    /**
     * Xerces-specific OutputFormat instance
     */
    private Object _outputFormat;
    
    /**
     * Creates an instance of this class. 
     */
    public XercesJDK5OutputFormat() {
        try {
            _outputFormat =  
            Class.forName(PACKAGE_NAME + ".OutputFormat").newInstance();
        } catch (Exception except) {
            throw new RuntimeException(
                    Messages.format("conf.failedInstantiateOutputFormat", 
                            PACKAGE_NAME + ".OutputFormat", except));
        }
    }
    
    /**
     * @see org.exolab.castor.xml.OutputFormat#setMethod(java.lang.String)
     * {@inheritDoc}
     */
    public void setMethod(final String method) {
    	Method aMethod;
		try {
			aMethod = _outputFormat.getClass().getMethod(
                    "setMethod", new Class[] {String.class} );
	    	aMethod.invoke(_outputFormat, new Object[] { method });
		} catch (Exception e) {
            String msg = "Problem invoking OutputFormat.setMethod()";
			LOG.error(msg, e);
			throw new RuntimeException(msg + e.getMessage()); 
		}
        // _outputFormat.setMethod(method);
    }

    /**
     * @see org.exolab.castor.xml.OutputFormat#setIndenting(boolean)
     * {@inheritDoc}
     */
    public void setIndenting(final boolean indent) {
    	Method method;
		try {
			method = _outputFormat.getClass().getMethod(
                    "setIndenting", new Class[] {boolean.class} );
	    	method.invoke(_outputFormat, new Object[] { Boolean.valueOf(indent) });
		} catch (Exception e) {
            String msg = "Problem invoking OutputFormat.setIndenting()";
			LOG.error(msg, e);
			throw new RuntimeException(msg + e.getMessage()); 
		}
        // _outputFormat.setIndenting(indent);
    }

    /**
     * @see org.exolab.castor.xml.OutputFormat#setPreserveSpace(boolean)
     * {@inheritDoc}
     */
    public void setPreserveSpace(final boolean preserveSpace) {
    	Method method;
		try {
			method = _outputFormat.getClass().getMethod(
                    "setPreserveSpace", new Class[] {boolean.class} );
	    	method.invoke(_outputFormat, new Object[] { Boolean.valueOf(preserveSpace) });
		} catch (Exception e) {
            String msg = "Problem invoking OutputFormat.setPreserveSpace()";
			LOG.error(msg, e);
			throw new RuntimeException(msg + e.getMessage()); 
		}
        // _outputFormat.setPreserveSpace(preserveSpace);
    }

    /**
     * @see org.exolab.castor.xml.OutputFormat#getFormat()
     * {@inheritDoc}
     */
    public Object getFormat() {
        return _outputFormat;
    }
    
    /**
     * @see org.exolab.castor.xml.OutputFormat#setDoctype(java.lang.String, java.lang.String)
     * {@inheritDoc}
     */
    public void setDoctype(final String type1, final String type2) {
    	Method method;
		try {
			method = _outputFormat.getClass().getMethod(
                    "setDoctype", new Class[] {String.class, String.class} );
	    	method.invoke(_outputFormat, new Object[] { type1, type2});
		} catch (Exception e) {
            String msg = "Problem invoking OutputFormat.setDoctype()";
			LOG.error(msg, e);
			throw new RuntimeException(msg + e.getMessage()); 
		}
        // _outputFormat.setDoctype(type1, type2);
    }

    /**
     * @see org.exolab.castor.xml.OutputFormat#setOmitXMLDeclaration(boolean)
     * {@inheritDoc}
     */
    public void setOmitXMLDeclaration(final boolean omitXMLDeclaration) {
    	Method method;
		try {
			method = _outputFormat.getClass().getMethod(
                    "setOmitXMLDeclaration", new Class[] {boolean.class} );
	    	method.invoke(_outputFormat, new Object[] { Boolean.valueOf(omitXMLDeclaration) });
		} catch (Exception e) {
            String msg = "Problem invoking OutputFormat.setOmitXMLDeclaration()";
			LOG.error(msg, e);
			throw new RuntimeException(msg + e.getMessage()); 
		}
        // _outputFormat.setOmitXMLDeclaration(omitXMLDeclaration);
    }

    /**
     * @see org.exolab.castor.xml.OutputFormat#setOmitDocumentType(boolean)
     * {@inheritDoc}
     */
    public void setOmitDocumentType(final boolean omitDocumentType) {
    	Method method;
		try {
			method = _outputFormat.getClass().getMethod(
                    "setOmitDocumentType", new Class[] {boolean.class} );
	    	method.invoke(_outputFormat, new Object[] { Boolean.valueOf(omitDocumentType) });
		} catch (Exception e) {
            String msg = "Problem invoking OutputFormat.setOmitDocumentType()";
			LOG.error(msg, e);
			throw new RuntimeException(msg + e.getMessage()); 
		}
        // _outputFormat.setOmitDocumentType(omitDocumentType);
    }

    /**
     * @see org.exolab.castor.xml.OutputFormat#setEncoding(java.lang.String)
     * {@inheritDoc}
     */
    public void setEncoding(final String encoding) {
    	Method method;
		try {
			method = _outputFormat.getClass().getMethod(
                    "setEncoding", new Class[] {String.class} );
	    	method.invoke(_outputFormat, new Object[] { encoding });
		} catch (Exception e) {
            String msg = "Problem invoking OutputFormat.setEncoding()";
			LOG.error(msg, e);
			throw new RuntimeException(msg + e.getMessage()); 
		}
        // _outputFormat.setEncoding(encoding);
    }
}
