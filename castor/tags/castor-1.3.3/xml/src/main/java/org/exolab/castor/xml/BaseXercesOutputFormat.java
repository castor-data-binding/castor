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

/**
 * Xerces-specific OutputFormat instance.
 * 
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @version $Revision: 7951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 */
public class BaseXercesOutputFormat implements OutputFormat {

    protected Object _outputFormat;
    
    public Object getFormat() {
        return _outputFormat;
    }
    
    public void setMethod(final String method) {
    	Method aMethod;
		try {
			aMethod = _outputFormat.getClass().getMethod(
                    "setMethod", new Class[] {String.class} );
	    	aMethod.invoke(_outputFormat, new Object[] { method });
		} catch (Exception e) {
            throw new RuntimeException("Problem invoking OutputFormat.setMethod()", e); 
		}
    }

    public void setIndenting(final boolean indent) {
    	Method method;
		try {
			method = _outputFormat.getClass().getMethod(
                    "setIndenting", new Class[] {boolean.class} );
	    	method.invoke(_outputFormat, new Object[] { Boolean.valueOf(indent) });
		} catch (Exception e) {
            throw new RuntimeException("Problem invoking OutputFormat.setIndenting()", e); 
		}
    }

    public void setPreserveSpace(final boolean preserveSpace) {
    	Method method;
		try {
			method = _outputFormat.getClass().getMethod(
                    "setPreserveSpace", new Class[] {boolean.class} );
	    	method.invoke(_outputFormat, new Object[] { Boolean.valueOf(preserveSpace) });
		} catch (Exception e) {
            throw new RuntimeException("Problem invoking OutputFormat.setPreserveSpace()", e); 
		}
    }

    public void setDoctype(final String type1, final String type2) {
    	Method method;
		try {
			method = _outputFormat.getClass().getMethod(
                    "setDoctype", new Class[] {String.class, String.class} );
	    	method.invoke(_outputFormat, new Object[] { type1, type2});
		} catch (Exception e) {
            throw new RuntimeException("Problem invoking OutputFormat.setDoctype()", e); 
		}
    }

    public void setOmitXMLDeclaration(final boolean omitXMLDeclaration) {
    	Method method;
		try {
			method = _outputFormat.getClass().getMethod(
                    "setOmitXMLDeclaration", new Class[] {boolean.class} );
	    	method.invoke(_outputFormat, new Object[] { Boolean.valueOf(omitXMLDeclaration) });
		} catch (Exception e) {
            throw new RuntimeException("Problem invoking OutputFormat.setOmitXMLDeclaration()", e); 
		}
    }

    public void setOmitDocumentType(final boolean omitDocumentType) {
        Method method;
        try {
            method = _outputFormat.getClass().getMethod("setOmitDocumentType",
                    boolean.class);
            method.invoke(_outputFormat, Boolean.valueOf(omitDocumentType));
        } catch (Exception e) {
            throw new RuntimeException("Problem invoking OutputFormat.setOmitDocumentType()", e);
        }
    }

    public void setEncoding(final String encoding) {
        Method method;
        try {
            method = _outputFormat.getClass().getMethod("setEncoding",
                    String.class);
            method.invoke(_outputFormat, encoding);
        } catch (Exception e) {
            throw new RuntimeException("Problem invoking OutputFormat.setEncoding()", e);
        }
    }
    
    public void setVersion(final String version) {
        Method method;
        try {
            method = _outputFormat.getClass().getMethod(
                    "setVersion", new Class[] {String.class} );
            method.invoke(_outputFormat, new Object[] { version });
        } catch (Exception e) {
            throw new RuntimeException("Problem invoking OutputFormat.setVersion()", e); 
        }
    }

}
