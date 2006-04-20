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
 *    permission of Exoffice Technologies.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Exoffice Technologies. Exolab is a registered
 *    trademark of Exoffice Technologies.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY EXOFFICE TECHNOLOGIES AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * EXOFFICE TECHNOLOGIES OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Exoffice Technologies Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml;

//-- xml related imports
import org.xml.sax.*;
import org.xml.sax.helpers.AttributeListImpl;
import org.exolab.castor.util.Configuration;

import java.io.IOException;
import java.io.Reader;
import java.io.PrintWriter;

/**
 * An unmarshaller to allowing unmarshalling of XML documents to
 * Java Objects. The Class must specify
 * the proper access methods (setters/getters) in order for instances
 * of the Class to be properly unmarshalled.
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class Unmarshaller {
    
    //----------------------------/
    //- Private Member Variables -/
    //----------------------------/
    
    /**
     * The Class that this Unmarshaller was created with
    **/
    Class _class = null;
    
    /**
     * The EntityResolver used for resolving entities
    **/
    EntityResolver entityResolver = null;

    /**
     * The print writer used for log information
    **/
    private PrintWriter pw = null;
    
    /**
     * The flag indicating whether or not to display debug information
    **/
    private boolean debug = false;
    
    //----------------/
    //- Constructors -/
    //----------------/
    
    /**
     * Creates a new Unmarshaller with the given Class
     * @param c the Class to create the Unmarshaller for
    **/
    public Unmarshaller(Class c) {
        super();
        this._class = c;
	this.debug = Configuration.debug();
    } //-- Unmarshaller(Class)
    
    
    /**
     * Turns debuging on or off. If no Log Writer has been set, then
     * System.out will be used to display debug information
     * @param debug the flag indicating whether to generate debug information.
     * A value of true, will turn debuggin on. 
     * @see #setLogWriter.
    **/
    public void setDebug(boolean debug) {
        this.debug = debug;
    } //-- setDebug
    
    /**
     * Sets the EntityResolver to use when resolving system and
     * public ids with respect to entites and Document Type.
     * @param entityResolver the EntityResolver to use when
     * resolving System and Public ids.
    **/
    public void setEntityResolver(EntityResolver entityResolver) {
        this.entityResolver = entityResolver;
    } //-- entityResolver
    
    /**
     * Sets the PrintWriter used for logging
     * @param printWriter the PrintWriter to use for logging
    **/
    public void setLogWriter(PrintWriter printWriter) {
        this.pw = printWriter;
    } //-- setLogWriter

    /**
     * Unmarshals Objects of this Unmarshaller's Class type. 
     * The Class must specify the proper access methods 
     * (setters/getters) in order for instances of the Class
     * to be properly unmarshalled.
     * @param reader the Reader to read the XML from
    **/
    public Object unmarshal(Reader reader) 
        throws java.io.IOException
    {
        return unmarshal(new InputSource(reader));
    } //-- unmarshal(Reader reader)

    /**
     * Unmarshals Objects of this Unmarshaller's Class type. 
     * The Class must specify the proper access methods 
     * (setters/getters) in order for instances of the Class
     * to be properly unmarshalled.
     * @param source the InputSource to read the XML from
    **/
    public Object unmarshal(InputSource source) 
        throws java.io.IOException
    {
        Parser parser = Configuration.getParser();
        
        if (parser == null)
            throw new IOException("unable to create parser");
            
        if (entityResolver != null)
            parser.setEntityResolver(entityResolver);
            
        UnmarshalHandler handler = new UnmarshalHandler(_class);
        handler.setLogWriter(pw);
        handler.setDebug(debug);
        parser.setDocumentHandler(handler);
            
        //parser.setErrorHandler(unmarshaller);
        try {
            parser.parse(source);
        }
        catch(org.xml.sax.SAXException sx) {
            
            String message = sx.getMessage();
            if (message == null) {
                Exception ex = sx.getException();
                if (ex != null) message = ex.getMessage();
            }
            if (message == null) 
                message = sx.toString();
            
            throw new java.io.IOException(message);
        }
        
        return handler.getObject();
    } //-- unmarshal(InputSource)
    
    /**
     * Unmarshals Objects of the given Class type. The Class must specify
     * the proper access methods (setters/getters) in order for instances
     * of the Class to be properly unmarshalled.
     * @param c the Class to create a new instance of
     * @param reader the Reader to read the XML from
    **/
    public static Object unmarshal(Class c, Reader reader) 
        throws java.io.IOException
    {
        Unmarshaller unmarshaller = new Unmarshaller(c);
        return unmarshaller.unmarshal(reader);;
    } //-- void unmarshal(Writer) 

    /**
     * Unmarshals Objects of the given Class type. The Class must specify
     * the proper access methods (setters/getters) in order for instances
     * of the Class to be properly unmarshalled.
     * @param c the Class to create a new instance of
     * @param source the InputSource to read the XML from
    **/
    public static Object unmarshal(Class c, InputSource source) 
        throws java.io.IOException
    {
        Unmarshaller unmarshaller = new Unmarshaller(c);
        return unmarshaller.unmarshal(source);;
    } //-- void unmarshal(Writer) 
    
} //-- Unmarshaller

