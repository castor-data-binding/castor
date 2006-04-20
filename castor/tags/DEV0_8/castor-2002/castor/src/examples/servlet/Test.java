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


package servlet;


import myapp.*;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.AttributeListImpl;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.JDO;
import org.exolab.castor.util.Logger;
import org.exolab.castor.xml.Marshaller;
import org.apache.xml.serialize.*;


/**
 * @author <a href="arkin@exoffice.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public class Test
    extends XMLServlet
{


    public static final String DatabaseFile = "/servlet/database.xml";
    

    public void service( HttpServletRequest request, XMLServletResponse response )
        throws ServletException
    {
        try {
            JDO           jdo;
            Database      db;
            OQLQuery      oql;
            Object        result;
            Marshaller    marshal;

            // Set the output type and stylesheet
            // response.setContentType( "text/html" );
            response.setOutputMethod( "html" );
            response.setStylesheet( new InputSource( getClass().getResourceAsStream( "xslt.xsl" ) ) );
            // For XML document
            /*
            response.setContentType( "text/xml" );
             */

            // Load the mapping from the specified filed, and Open up the
            // database for read/write access
            jdo = new JDO();
            jdo.setDatabaseName( "test" );
            jdo.setConfiguration( getClass().getResource( DatabaseFile ).toString() );
            db = jdo.getDatabase();

            // All ODMG database access requires a transaction
            db.begin();

            // Construct a new query to load all the products in the database
            oql = db.getOQLQuery( "SELECT p FROM myapp.Product p" );
            result = oql.execute();

            marshal = new Marshaller( response.getDocumentHandler() );

            // Print all the products as elements under the root element
            // <products>, using the Marshaller.
            response.getDocumentHandler().startDocument();
            response.getDocumentHandler().startElement( "products", new AttributeListImpl() );
            if ( result instanceof Enumeration ) {
                Enumeration enum;

                enum = (Enumeration) result;
                while( enum.hasMoreElements() )
                   marshal.marshal( enum.nextElement() );
            } else 
                marshal.marshal( result );

            // Close the document, transaction and database.
            response.getDocumentHandler().endElement( "products" );
            response.getDocumentHandler().endDocument();
            db.commit();
            db.close();

        } catch ( Exception except ) {
            System.out.println( except );
            except.printStackTrace();
            throw new ServletException( except.toString() );
        }
    }


}
