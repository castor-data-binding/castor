/*
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
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.xml;

import java.util.Vector;

/**
 * A very simple XPath location class for use with the ValidationException. This
 * class only supports the parent "/" operator and element names.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2003-03-03 00:05:44 -0700 (Mon, 03 Mar 2003) $
 */
public class XPathLocation implements Location {

    /** Our XPath, built up one String at a time. */
    private final Vector path                = new Vector();
    /** If we have reached the logical end of XPath (i.e., an attribute), set to false. */
    private boolean      allowChildrenOrAtts = true;

    /**
     * Creates a default XPathLocation.
     */
    public XPathLocation() {
        super();
    }

    /**
     * Adds an attribute to the XPath location.
     * @param name the name of the attribute
     */
    public void addAttribute(final String name) {
        if (allowChildrenOrAtts) {
            allowChildrenOrAtts = false;
            path.addElement("@" + name);
        }
    }

    /**
     * Adds the given element name as a child of the current path.
     * @param name the name to add as a child
     */
    public void addChild(final String name) {
        if (allowChildrenOrAtts) {
            path.addElement(name);
        }
    }

    /**
     * Adds the name as a parent of the current path.
     * @param name the name to add as a parent
     */
    public void addParent(final String name) {
        path.insertElementAt(name, 0);
    }

    /**
     * Returns the String representation of this XPathLocation.
     * @return the String representation of this XPathLocation.
     */
    public String toString() {
        StringBuffer buf = new StringBuffer("XPATH: ");

        for (int i = 0; i < path.size(); i++) {
            buf.append('/');
            buf.append((String)path.elementAt(i));
        }
        return buf.toString();
    }

}
