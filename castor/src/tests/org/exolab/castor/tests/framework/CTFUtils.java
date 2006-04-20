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
 * Copyright 2002 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 *
 */

package org.exolab.castor.tests.framework;

import org.exolab.adaptx.xslt.dom.XPNReader;
import org.exolab.adaptx.xslt.dom.XPNBuilder;
import org.exolab.adaptx.xpath.XPathNode;
import org.exolab.adaptx.xml.XMLDiff;

//-- Java imports
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;


/**
 * This class contains utility methods needed by the CTF.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$ $Date$
 */

public class CTFUtils {

   /**
	* Loads the given XML file as an XPathNode
    *
	* @param url the filename or URL of the XML file to load
    */
    public static XPathNode loadXPN(String url) throws java.io.IOException {

	    XPathNode node = null;
	    XPNReader reader = new XPNReader(url);
	    reader.setSaveLocation(true);
	    node = reader.read();
        return node;
    } //-- loadXPN

   /**
    * Compares two XML documents located at 2 given URL.
    * @param document1 the URL of the first XML document.
    * @param document2 the URL of the second XML document.
    * @return an int indicating the number of differences or 0 if both documents are
    * 'XML equivalent'.
    */
    public static int compare(String document1, String document2) throws java.io.IOException {
        XPathNode node1 = loadXPN(document1);
        XPNReader reader = new XPNReader(document2);
        XPathNode node2 = reader.read();
	    XMLDiff diff = new XMLDiff();
	    int result = diff.compare(node1, document1, node2, "In-Memory-Result");
        return result;

    }


}