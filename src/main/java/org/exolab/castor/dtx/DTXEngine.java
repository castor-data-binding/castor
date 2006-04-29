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
 * Copyright 2000 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.dtx;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.sql.*;
import org.xml.sax.*;
import org.castor.jdo.conf.*;
import org.exolab.castor.persist.*;
import org.exolab.castor.persist.spi.PersistenceFactory;
import org.exolab.castor.xml.*;
import org.exolab.castor.mapping.xml.MappingRoot;
import org.exolab.castor.mapping.xml.ClassMapping;
import org.exolab.castor.util.*;
import org.exolab.castor.xml.schema.*;
import org.exolab.castor.xml.schema.reader.*;

/**
 * An OQL query engine for hitting JDO data sources and generating SAX
 * events. It skips the intermediate Java class step used by
 * e.g. Castor JDO.
 *
 * The engine uses the SQL and XML descriptions in an XML Schema file
 * and a Castor JDO mapping file to determine how to map results of
 * the query into SAX events.
 *
 * @author <a href="0@intalio.com">Evan Prodromou</a>
 * @version $Revision$ $Date$
 */

public class DTXEngine {

    protected DocumentHandler _handler = null;
    protected String _databaseURL = null;
    protected Database _database = null;
    protected String _schemaURL = null;
    protected Schema _schema = null;
    protected HashMap _classMappings = null;
    protected Connection _conn = null;
    protected PersistenceFactory _factory = null;

    /**
     * Default constructor.
     */
    public DTXEngine() {
        super();
    } //-- DTXEngine

    /**
     * Construct a DTXEngine for the given JDO mapping file and
     * XML schema.
     *
     * @param databaseURL URL string for JDO mapping file.
     * @param schemaURL URL string for XML Schema file.
     */

    public DTXEngine(String databaseURL, String schemaURL)
	    throws DTXException
    {
	    setDatabase(databaseURL);
	    setSchema(schemaURL);
    }

    /**
     * Sets the XML Schema to use. Parses and prepares the Schema.
     *
     * @param schemaURL URL string for XML Schema file.  */

    public void setSchema(String schemaURL) 
        throws DTXException
    {
	    _schemaURL = schemaURL;

        try {
            SchemaReader 
                reader = new SchemaReader(new InputSource((new URL(schemaURL)).openStream()));
            _schema = reader.read();
        }
        catch(IOException iox) {
            throw new DTXException(iox);
        }
        catch(Exception e) {
            throw new DTXException(e);
        }
    }

    /**
     * Sets the database options from a JDO database mapping file.
     *
     * @param databaseURL URL string for JDO database mapping file.
     */

    public void setDatabase(String databaseURL) throws DTXException {
	_databaseURL = databaseURL;
        Unmarshaller unm = new Unmarshaller( Database.class );

	unm.setEntityResolver( new DTDResolver() );

	try {
	    _database = (Database) unm.unmarshal(new InputSource((new URL(databaseURL)).openStream()));
	} catch (Exception except) {
	    throw new DTXException(except);
	}

	if ( _database.getEngine() == null  ) {
	    _factory = PersistenceFactoryRegistry.getPersistenceFactory("generic");
	} else {
	    _factory = PersistenceFactoryRegistry.getPersistenceFactory(_database.getEngine());
	}

	if ( _factory == null ) {
	    throw new DTXException("no engine");
	}

	// Class mappings

	_classMappings = new HashMap();

	// Load the specificed mapping source

	Unmarshaller munm = new Unmarshaller( MappingRoot.class );

 	Mapping mappings[] = _database.getMapping();

	for ( int i = 0 ; i < mappings.length ; ++i ) {
	    try {
		URL mappingURL = new URL(new URL(databaseURL), mappings[i].getHref());
		MappingRoot mr = (MappingRoot) munm.unmarshal(new InputSource((mappingURL).openStream()));
		ClassMapping[] classMaps = mr.getClassMapping();

		for (int j = 0; j < classMaps.length; j++) {
		    _classMappings.put(classMaps[j].getName(), classMaps[j]);
		}
	    } catch (Exception e) {
		throw new DTXException(e);
	    }
 	}
    }

    /**
     * Sets the default SAX document handler for this DTX
     * engine. Individual queries will use this handler by default,
     * but it can be overwritten on a per-query basis.
     *
     * @param handler A DocumentHandler to receive query results as
     * SAX events.
     */

    public void setDocumentHandler(DocumentHandler handler) {
	_handler = handler;
    }

    /**
     * Prepare a new DTXQuery object, given an OQL string. The syntax
     * is currently limited only to SELECT statements that return a
     * single object type (although multiple results will appear as
     * multiple documents to the DocumentHandler).
     *
     * @param oql OQL string for the query.
     */

    public DTXQuery prepareQuery(String oql) throws DTXException {

	DTXQuery qry = new DTXQuery();
	qry.setEngine(this);
	qry.setHandler(_handler);
	qry.prepare(oql);

	return qry;
    }

    /* Package scope -- for DTXQuery */

    /* Returns the Database mapping information. */

    Database getDatabase() {
	return _database;
    }

    /* Returns the parsed XML Schema. */

    Schema getSchema() {
	return _schema;
    }

    /* The Castor persistence factory for this engine, derived from
       the Database mapping. This is used to create new
       QueryExpression (an abstraction of a SQL query). */

    PersistenceFactory getFactory() {
	return _factory;
    }

    /* Get a (single) connection from this engine. Yes, this needs
       serious re-work. It should be re-written to work with the JDO
       and/or Tyrex pooling mechanism. */

    Connection getConnection() throws DTXException {
	if (_conn == null) {
	    DataSource datasource = _database.getDatabaseChoice().getDataSource();
	    org.castor.jdo.conf.Driver driver = _database.getDatabaseChoice().getDriver();

	    if (datasource != null) {
		// FIXME: lame
		throw new DTXException("dtx.DataSourceUnimplemented");
	    } else if (driver != null) {
		try {
		    String className = driver.getClassName();
		    Class.forName(className);
		    String jdbcUrl = driver.getUrl();
		    Param[] params = driver.getParam();
		    Properties props = new Properties();

		    for (int i = 0; i < params.length; i++) {
			Param p = params[i];
			props.setProperty(p.getName(), p.getValue());
		    }

		    _conn = DriverManager.getConnection(jdbcUrl, props);
		} catch (Exception e) {
		    throw new DTXException(e);
		}
	    }
	}
	return _conn;
    }

    /* Return a class mapping based on the class name. */

    ClassMapping getClassMapping(String className) {
	return (ClassMapping) _classMappings.get(className);
    }
}
