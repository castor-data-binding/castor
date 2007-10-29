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
 * Copyright 2001-2002 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;

import org.castor.xml.AbstractInternalContext;
import org.castor.xml.BackwardCompatibilityContext;
import org.castor.xml.InternalContext;
import org.exolab.castor.util.NestedIOException;
import org.exolab.castor.xml.schema.Order;
import org.exolab.castor.xml.schema.Schema;
import org.exolab.castor.xml.schema.writer.SchemaWriter;
import org.xml.sax.InputSource;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;

/**
 * A class for reading XML Schemas.
 * 
 * To generate an XML schema from a given XML document instance and write it to 
 * a file, please use code similar to the following:
 *
 * InputSource inputSource = ...;
 * XMLInstance2Schema xi2s = new XMLInstance2Schema();
 * Schema schema = xi2s.createSchema(inputSource);
 *
 * Writer dstWriter = new FileWriter(...);
 * xi2s.serializeSchema(dstWriter, schema);
 * dstWriter.close();
 * 
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2006-01-16 13:22:58 -0700 (Mon, 16 Jan
 *          2006) $
 */
public class XMLInstance2Schema {

    /**
     * The {@link AbstractInternalContext} used to get Parser from.
     */
    private InternalContext _internalContext;

    private Order _defaultGroup = Order.seq;

    /**
     * Creates a new XMLInstance2Schema
     * 
     */
    public XMLInstance2Schema() {
        super();
        _internalContext = new BackwardCompatibilityContext();
    }

    /**
     * Creates an XML Schema using the given XML instance filename. The XML
     * Schema created will be based on the specific XML instance document.
     * 
     * @param filename
     *            the filename for the XML document
     */
    public Schema createSchema(String filename) throws IOException {
        return createSchema(new InputSource(filename));
    }

    /**
     * Creates an XML Schema using the given Reader. The reader must be for an
     * XML instance document. The XML Schema created will be based on the
     * specific XML instance document.
     * 
     * @param reader
     *            the Reader for the XML document
     */
    public Schema createSchema(Reader reader) throws IOException {
        return createSchema(new InputSource(reader));
    }

    /**
     * Creates an XML Schema using the given InputSource. The InputSource must
     * be for an XML instance document. The XML Schema created will be based on
     * the specific XML instance document.
     * 
     * @param source
     *            the InputSource for the XML document
     */
    public Schema createSchema(InputSource source) throws IOException {
        XMLInstance2SchemaHandler handler = new XMLInstance2SchemaHandler();
        handler.setDefaultGroupOrder(_defaultGroup);

        try {
            Parser parser = _internalContext.getParser();
            if (parser == null) {
                throw new IOException(
                        "fatal error: unable to create SAX parser.");
            }
            parser.setDocumentHandler(handler);
            parser.setErrorHandler(handler);
            parser.parse(source);
        } catch (org.xml.sax.SAXException sx) {
            throw new NestedIOException(sx);
        }
        return handler.getSchema();
    }

    /**
     * Sets the default grouping as "all". By default groups will be treated as
     * "sequence".
     */
    public void setDefaultGroupingAsAll() {
        _defaultGroup = Order.all;
    }

    /**
     * Serializes a {@link Schema} instance to the given {@link Writer} instance.
     * @param dstWriter The {@link Writer} instance to output the XML schema to.
     * @param schema The XML {@link Schema} instance to be output.
     * @throws IOException If there's a problem related to writing to the given {@link Writer} instance. 
     * @throws SAXException If there's a problem related to SAX streaming.
     */
    public void serializeSchema(Writer dstWriter, Schema schema)
            throws IOException, SAXException {
        SchemaWriter schemaWriter = new SchemaWriter(dstWriter);
        schemaWriter.write(schema);
    }

    /**
     * For testing purposes only.
     * @deprecate Please see class documentation for an example of how to use
     * this class. Or consider using the Ant task for the schema generator.
     */
    public static void main(String args[]) {

        if (args.length == 0) {
            System.out.println("Missing filename");
            System.out.println();
            System.out
                    .println("usage: java XMLInstance2Schema <input-file> [<output-file> (optional)]");
            return;
        }

        try {
            XMLInstance2Schema xi2s = new XMLInstance2Schema();
            Schema schema = xi2s.createSchema(args[0]);

            Writer dstWriter = null;
            if (args.length > 1) {
                dstWriter = new FileWriter(args[1]);
            } else {
                dstWriter = new PrintWriter(System.out, true);
            }

            xi2s.serializeSchema(dstWriter, schema);
            dstWriter.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
