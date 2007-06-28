/*
 * Copyright 2007 Edward Kuns
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
 *
 * $Id: XMLReader.java 0000 2007-01-11 00:00:00Z ekuns $
 */
package org.castor.xmlctf.xmldiff.xml;

import java.io.File;
import java.io.FileReader;
import java.net.MalformedURLException;

import org.castor.xmlctf.xmldiff.xml.nodes.Root;
import org.castor.xmlctf.xmldiff.xml.nodes.XMLNode;
import org.exolab.castor.util.NestedIOException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

/**
 * A Utility class to read an XML document from a file into the XMLNode tree
 * used by the XMLDiff class.
 *
 * @author <a href="mailto:edward.kuns@aspect.com">Edward Kuns</a>
 * @version $Revision: 0000 $ $Date: 2007-01-11 00:00:00 -0600 (Thu, 11 Jan 2007) $
 * @see org.castor.xmlctf.xmldiff.XMLDiff
 * @since Castor 1.1
 */
public class XMLFileReader {

    /** The file we are reading. */
    private final File      _file;
    /** URL for the document to be parsed. */
    private final String    _location;
    /** A handle to the SAX parser. */
    private final XMLReader _parser;

    /**
     * Creates a new XMLReader for the given URILocation.
     *
     * @param filename the URILocation to create this reader for.
     */
    public XMLFileReader(final String filename) {
        if (filename == null) {
            throw new IllegalArgumentException("You must give a non-null fliename");
        }
        _file = new File(filename);
        if (!_file.exists()) {
            throw new IllegalArgumentException("File '" + filename + "' does not exist");
        }

        _location = getUrlFromFile();
        _parser   = new org.apache.xerces.parsers.SAXParser();
    }

    /**
     * Reads an XML Document into an BaseNode from the provided file.
     *
     * @return the BaseNode
     * @throws java.io.IOException if any exception occurs during parsing
     */
    public XMLNode read() throws java.io.IOException {
        XMLNode node = null;

        try {
            InputSource source = new InputSource();
            source.setSystemId(_location);
            source.setCharacterStream(new FileReader(_file));

            XMLContentHandler builder = new XMLContentHandler();

            _parser.setContentHandler(builder);
            _parser.parse(source);

            node = builder.getRoot();
        } catch (SAXException sx) {
            Exception nested = sx.getException();

            SAXParseException sxp = null;
            if (sx instanceof SAXParseException) {
                sxp = (SAXParseException) sx;
            } else if (nested != null && (nested instanceof SAXParseException)) {
                sxp = (SAXParseException) nested;
            } else {
                throw new NestedIOException(sx);
            }

            StringBuffer err = new StringBuffer(sxp.toString());
            err.append("\n - ");
            err.append(sxp.getSystemId());
            err.append("; line: ");
            err.append(sxp.getLineNumber());
            err.append(", column: ");
            err.append(sxp.getColumnNumber());
            throw new NestedIOException(err.toString(), sx);
        }

        Root root = (Root) node;
        return root;
    }

    /**
     * Returns the absolute URL as a string.
     * @param file The URL to resolve
     * @return the absolute URL as a string.
     */
    private String getUrlFromFile() {
        try {
            return _file.toURL().toString();
        } catch (MalformedURLException e) {
            // ignore -- cannot happen
        }
        return null;
    }

}
