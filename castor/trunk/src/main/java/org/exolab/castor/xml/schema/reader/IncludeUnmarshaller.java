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
 * Copyright 1999-2004 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema.reader;

//-- imported classes and packages
import org.exolab.castor.net.URIException;
import org.exolab.castor.net.URILocation;
import org.exolab.castor.net.URIResolver;
import org.exolab.castor.xml.AttributeSet;
import org.exolab.castor.xml.XMLException;
import org.exolab.castor.xml.schema.Schema;
import org.exolab.castor.xml.schema.SchemaContext;
import org.exolab.castor.xml.schema.SchemaException;
import org.exolab.castor.xml.schema.SchemaNames;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.Parser;

/**
 * A simple unmarshaller to read included schemas.
 * Included schemas can be cached in the original parent Schema or
 * can be inlined in that same XML Schema as recommended by the XML Schema
 * specification.
 * 
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 **/
public class IncludeUnmarshaller extends ComponentReader
{


    public IncludeUnmarshaller(
            final SchemaContext schemaContext,
            final Schema schema,
            final AttributeSet atts,
            final URIResolver uriResolver,
            final Locator locator,
            final SchemaUnmarshallerState state)
    throws XMLException {
        super(schemaContext);

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
            include = uri.getAbsoluteURI();

        //-- Has this schema location been included yet?
        if (schema.includeProcessed(include)) {
            return;
        }
        else if (include.equals(schema.getSchemaLocation())) {
            return;
        }
        
        Schema includedSchema = null;
        boolean alreadyLoaded = false;

        //-- caching is on
        if (state.cacheIncludedSchemas) {
            if (uri instanceof SchemaLocation) {
        		includedSchema = ((SchemaLocation)uri).getSchema();
        		schema.cacheIncludedSchema(includedSchema);
        		alreadyLoaded = true;
        	}
        	//-- Have we already imported this XML Schema file?
	        if (state.processed(include)) {
	        	includedSchema = state.getSchema(include);
	        	schema.cacheIncludedSchema(includedSchema);
	        	alreadyLoaded = true;
	        }
        }
	    
        if (includedSchema == null)
	    	includedSchema = new Schema();
        else
        	state.markAsProcessed(include, includedSchema);
        
        //-- keep track of the schemaLocation
        schema.addInclude(include);

        if (alreadyLoaded)
        	return;
		Parser parser = null;
		try {
		    parser = getSchemaContext().getParser();
		}
		catch(RuntimeException rte) {}
		if (parser == null) {
		    throw new SchemaException("Error failed to create parser for include");
		}
		SchemaUnmarshaller schemaUnmarshaller = new SchemaUnmarshaller(getSchemaContext(), true, state, getURIResolver());

		if (state.cacheIncludedSchemas)
		    schemaUnmarshaller.setSchema(includedSchema);
		else
			schemaUnmarshaller.setSchema(schema);
		
		Sax2ComponentReader handler = new Sax2ComponentReader(schemaUnmarshaller);
		parser.setDocumentHandler(handler);
		parser.setErrorHandler(handler);

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
		if (state.cacheIncludedSchemas) {
			String ns = includedSchema.getTargetNamespace();
			if (ns == null || ns == "")
				includedSchema.setTargetNamespace(schema.getTargetNamespace());
			else if (!ns.equals(schema.getTargetNamespace()))
				throw new SchemaException("The target namespace of the included components must be the same as the target namespace of the including schema");
			schema.cacheIncludedSchema(includedSchema);
		}
	}


    /**
     * Sets the name of the element that this UnknownUnmarshaller handles
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
