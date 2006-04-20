/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Intalio, Inc.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Intalio, Inc. Exolab is a registered
 *    trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999-2002 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema.reader;

//-- imported classes and packages
import org.exolab.castor.net.*;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.schema.*;
import org.exolab.castor.util.Configuration;
import org.xml.sax.*;

import java.net.URL;
import java.util.Vector;

public class IncludeUnmarshaller extends ComponentReader
{


    public IncludeUnmarshaller
        (Schema schema, AttributeSet atts, Resolver resolver, URIResolver uriResolver,
         Locator locator, SchemaUnmarshallerState state)
		throws XMLException
    {
        super();
        setResolver(resolver);
        setURIResolver(uriResolver);
        URILocation uri = null;
		//-- Get schemaLocation
		String include = atts.getValue("schemaLocation");
		if (include == null)
			throw new SchemaException("'schemaLocation' attribute missing on 'include'");

		if (include.indexOf("\\") != -1) {
            String err = include+" is not a valid URI as defined by IETF RFC 2396.";
            err += "The URI must not contain '\\'.";
            throw new SchemaException(err);
		}

        try {
            String documentBase = locator.getSystemId();
            if (documentBase != null) {
                if (!documentBase.endsWith("/"))
                    documentBase = documentBase.substring(0, documentBase.lastIndexOf("/") +1 );
            }
		    uri = getURIResolver().resolve(include, documentBase);
        } catch (URIException ure) {
            throw new XMLException(ure);
        }

        if (uri != null)
            include = uri.toString();

        //-- Has this schema location been included yet?
        if (schema.includeProcessed(include)) {
            return;
        }
        else if (include.equals(schema.getSchemaLocation())) {
            return;
        }
        
        //-- keep track of the schemaLocation
        schema.addInclude(include);

		Parser parser = null;
		try {
		    parser = state.getConfiguration().getParser();
		}
		catch(RuntimeException rte) {}
		if (parser == null) {
		    throw new SchemaException("Error failed to create parser for include");
		}
		else
		{
			SchemaUnmarshaller schemaUnmarshaller = new SchemaUnmarshaller(true, state, getURIResolver());
            schemaUnmarshaller.setSchema(schema);
			Sax2ComponentReader handler = new Sax2ComponentReader(schemaUnmarshaller);
			parser.setDocumentHandler(handler);
			parser.setErrorHandler(handler);
		}

		try {
            InputSource source = new InputSource(uri.getReader());
            source.setSystemId(uri.getAbsoluteURI());
            parser.parse(source);
		}
		catch(java.io.IOException ioe) {
		    throw new SchemaException("Error reading include file '"+include+"'");
		}
		catch(org.xml.sax.SAXException sx) {
		    throw new SchemaException(sx);
		}
	}


    /**
     * Sets the name of the element that this UnknownUnmarshaller handles
     * @param name the name of the element that this unmarshaller handles
    **/
    public String elementName() {
        return SchemaNames.INCLUDE;
    } //-- elementName

    /**
     * Returns the Object created by this ComponentReader
     * @return the Object created by this ComponentReader
    **/
    public Object getObject() {
        return null;
    } //-- getObject

}
