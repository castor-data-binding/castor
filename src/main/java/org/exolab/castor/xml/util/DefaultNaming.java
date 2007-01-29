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
 * Copyright 1999, 2000 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.xml.util;

import org.exolab.castor.xml.XMLNaming;

/**
 * The default implementation of org.exolab.castor.xml.Naming
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2003-03-03 00:05:44 -0700 (Mon, 03 Mar 2003) $
 */
public final class DefaultNaming extends XMLNaming {

    /**
     * The lower case style with hyphens to separate words. <I>Default</I>
     * <BR />
     * <B>examples:</B><BR />
     * "Blob" becomes "blob" and "DataSource" becomes "data-source".
     */
    public static final short LOWER_CASE_STYLE = 0;

    /**
     * The mixed case style with uppercase characters to separate words. <BR>
     * <B>examples:</B><BR>
     * "Blob" becomes "blob" and "DataSource" becomes "dataSource".
     */
    public static final short MIXED_CASE_STYLE = 1;

    private static short _style = LOWER_CASE_STYLE;

    /**
     * Creates a new DefaultNaming
     */
    public DefaultNaming() {
        super();
    } //-- DefaultNaming

    /**
     * Sets the style for this DefaultNaming.
     * Valid options are as follows
     * <pre>
     *   DefaultNaming.LOWER_CASE_STYLE
     *   DefaultNaming.MIXED_CASE_STYLE
     * </pre>
     * @param style the style to use
     */
    public void setStyle(final short style) {
        switch (style) {
            case MIXED_CASE_STYLE:
            case LOWER_CASE_STYLE:
                _style = style;
                break;
            default:
                throw new IllegalArgumentException("Invalid option for DefaultNaming#setStyle.");
        }
    } //-- setStyle

    /**
     * Creates the XML Name for the given class. It would be nearly impossible
     * for this method to please every one, so I picked common "de-facto" XML
     * naming conventions. This can be overridden by either extending
     * org.exolab.castor.xml.Naming and implementing the proper methods, or by
     * ClassDescriptors for your classes.
     *
     * @param c the Class to create the XML Name for
     * @return the xml name representation of the given String <BR>
     *         <B>examples:</B><BR>
     *         "Blob" becomes "blob" and "DataSource" becomes "data-source".
     * @see org.exolab.castor.xml.XMLNaming
     */
    public String createXMLName(final Class c) {
        //-- create default XML name
        String name = c.getName();
        int idx = name.lastIndexOf('.');
        if (idx >= 0) {
            name = name.substring(idx+1);
        }
        return toXMLName(name);
    } //-- createXMLName

    /**
     * Converts the given name to an XML name. It would be nearly impossible for
     * this method to please every one, so I picked common "de-facto" XML naming
     * conventions. This can be overridden by either extending
     * org.exolab.castor.xml.Naming and implementing the proper methods, or by
     * ClassDescriptors for your classes.
     *
     * @param name the String to convert to an XML name
     * @return the xml name representation of the given String <BR>
     *         <B>examples:</B><BR>
     *         "Blob" becomes "blob" and "DataSource" becomes "data-source".
     * @see org.exolab.castor.xml.XMLNaming
     */
    public String toXMLName(final String name) {
        if (name == null) {
            return null;
        }
        if (name.length() == 0) {
            return name;
        }
        if (name.length() == 1) {
            return name.toLowerCase();
        }

        //-- Follow the Java beans Introspector::decapitalize
        //-- convention by leaving alone String that start with
        //-- 2 uppercase characters.
        if (Character.isUpperCase(name.charAt(0)) && Character.isUpperCase(name.charAt(1))) {
            return name;
        }

        //-- process each character
        StringBuffer cbuff = new StringBuffer(name);
        cbuff.setCharAt(0, Character.toLowerCase(cbuff.charAt(0)));

        boolean ucPrev = false;
        for (int i = 1; i < cbuff.length(); i++) {
            char ch = cbuff.charAt(i);
            if (Character.isUpperCase(ch)) {
                if (ucPrev) {
                    continue;
                }
                ucPrev = true;
                if (_style == LOWER_CASE_STYLE) {
                    cbuff.insert(i,'-');
                    ++i;
                    cbuff.setCharAt(i, Character.toLowerCase(ch));
                } else {
                    ++i;
                }
            } else if (ch == '.') {
                //-- do not add '-' if preceeded by '.'
                ucPrev = true;
            } else {
                ucPrev = false;
            }
        }
        return cbuff.toString();
    } //-- toXMLName

} //-- Naming
