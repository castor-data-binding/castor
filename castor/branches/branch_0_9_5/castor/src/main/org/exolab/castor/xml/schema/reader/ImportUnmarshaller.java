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
import org.exolab.castor.net.*;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.schema.*;
import org.exolab.castor.util.Configuration;
import org.xml.sax.*;

import java.net.URL;
import java.util.Vector;

public class ImportUnmarshaller extends ComponentReader
{


    public ImportUnmarshaller
        (Schema schema, AttributeSet atts, Resolver resolver, URIResolver uriResolver, Locator locator, SchemaUnmarshallerState state)
		throws XMLException
    {
        super();
        setResolver(resolver);
        setURIResolver(uriResolver);

        URILocation uri = null;
		//-- Get schemaLocation
		String schemaLocation = atts.getValue(SchemaNames.SCHEMALOCATION_ATTR);
		//-- Get namespace
		String namespace = atts.getValue("namespace");
		
		if ((schemaLocation == null) && (namespace == null)) {
		    //-- A legal <import/> element...just return
		    return; 
		}

        boolean hasLocation = (schemaLocation != null);
        if (schemaLocation != null) {
            
            if (schemaLocation.indexOf("\\") != -1) {
                String err = "'" + schemaLocation + 
                    "' is not a valid URI as defined by IETF RFC 2396.";
                err += "The URI mustn't contain '\\'.";
                throw new SchemaException(err);
		    }

            if (namespace == null) namespace = "";
            
            try {
                String documentBase = locator.getSystemId();
                if (documentBase != null) {
                    if (!documentBase.endsWith("/"))
                        documentBase = documentBase.substring(0, documentBase.lastIndexOf('/') +1 );
                }
		        uri = getURIResolver().resolve(schemaLocation, documentBase);
                if (uri != null) {
                    schemaLocation = uri.getAbsoluteURI();
                }
            } 
            catch (URIException urix) {
                throw new XMLException(urix);
            }
        }
        else {
            schemaLocation = namespace;
            try {
		        uri = getURIResolver().resolveURN(namespace);
            } 
            catch (URIException urix) {
                throw new XMLException(urix);
            }
            if (uri == null) {
                String err = "Unable to resolve Schema corresponding " +
                    "to namespace '" + namespace + "'.";
                throw new SchemaException(err);
                    
            }
        }

        //-- Make sure targetNamespace is not the same as the
        //-- importing schema, see section 4.2.3 in the
        //-- XML Schema Recommendation
        if (namespace.equals(schema.getTargetNamespace()) )
            throw new SchemaException("the 'namespace' attribute in the <import> element cannot be the same of the targetNamespace of the global schema");

        //-- Schema object to hold import schema
		boolean addSchema = false;
		Schema importedSchema = schema.getImportedSchema(namespace, true);

        //-- Have we already imported this XML Schema file?
		if (state.processed(schemaLocation)) {
           if (importedSchema == null)
               schema.addImportedSchema(state.getSchema(schemaLocation));
           return;
		}
		
		boolean alreadyLoaded = false;
        if (importedSchema == null) {
            if (uri instanceof SchemaLocation) {
                importedSchema = ((SchemaLocation)uri).getSchema();
			    schema.addImportedSchema(importedSchema);
			    alreadyLoaded = true;
            }
            else {
			    importedSchema = new Schema();
			    addSchema = true;
			}
		}
		else {
		    //-- check schema location, if different, allow merge
		    if (hasLocation) {
		        String tmpLocation = importedSchema.getSchemaLocation();
		        alreadyLoaded = schemaLocation.equals(tmpLocation);
		    }
		    else { 
		        //-- only namespace can be used, no way to distinguish
		        //-- multiple imports...mark as alreadyLoaded
		        //-- see W3C XML Schema 1.0 Recommendation (part 1)
		        //-- section 4.2.3...
		        //-- <quote>... Given that the schemaLocation [attribute] is only 
		        //--   a hint, it is open to applications to ignore all but the 
		        //--   first <import> for a given namespace, regardless of the 
		        //--  ·actual value· of schemaLocation, but such a strategy
                //--  risks missing useful information when new schemaLocations 
                //--  are offered.</quote>
		        alreadyLoaded = true;
		    }
		}

        state.markAsProcessed(schemaLocation, importedSchema);

        if (alreadyLoaded) return;
        
        //-- Parser Schema
		Parser parser = null;
		try {
		    parser = state.getConfiguration().getParser();
		}
		catch(RuntimeException rte) {}
		if (parser == null) {
		    throw new SchemaException("Error failed to create parser for import");
		}
		else {
			//-- Create Schema object and setup unmarshaller
			SchemaUnmarshaller schemaUnmarshaller = new SchemaUnmarshaller(state);
            schemaUnmarshaller.setURIResolver(getURIResolver());
			schemaUnmarshaller.setSchema(importedSchema);
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
		    throw new SchemaException("Error reading import file '"+schemaLocation+"': "+ ioe);
		}
		catch(org.xml.sax.SAXException sx) {
		    throw new SchemaException(sx);
		}

		//-- Add schema to list of imported schemas (if not already present)
		if (addSchema)
		{
            importedSchema.setSchemaLocation(schemaLocation);
			schema.addImportedSchema(importedSchema);
		}
	}


    /**
     * Sets the name of the element that this UnknownUnmarshaller handles
     * @param name the name of the element that this unmarshaller handles
    **/
    public String elementName() {
        return SchemaNames.IMPORT;
    } //-- elementName

    /**
     * Returns the Object created by this ComponentReader
     * @return the Object created by this ComponentReader
    **/
    public Object getObject() {
        return null;
    } //-- getObject

}
