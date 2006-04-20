/*
 * (C) Copyright Keith Visco 1998, 1999  All rights reserved.
 *
 * The contents of this file are released under an Open Source 
 * Definition (OSD) compliant license; you may not use this file 
 * execpt in compliance with the license. Please see license.txt, 
 * distributed with this file. You may also obtain a copy of the
 * license at http://www.clc-marketing.com/xslp/license.txt
 *
 * The program is provided "as is" without any warranty express or
 * implied, including the warranty of non-infringement and the implied
 * warranties of merchantibility and fitness for a particular purpose.
 * The Copyright owner will not be liable for any damages suffered by
 * you as a result of using the Program. In no event will the Copyright
 * owner be liable for any special, indirect or consequential damages or
 * lost profits even if the Copyright owner has been advised of the
 * possibility of their occurrence.
 *
 * The orginal source for this file is XSL:P
 *
 * $Id$
 */

package org.exolab.castor.xml.util;


import org.xml.sax.AttributeList;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;


/**
 * A class which implements AttributeList by "wrapping" a DOM
 * NamedNodeMap.
 * XSLReader when reading an XSLT stylsheet.
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class AttributeListWrapper implements AttributeList {


    NamedNodeMap _map = null;

    public AttributeListWrapper(NamedNodeMap namedNodeMap) {
        _map = namedNodeMap;
    } //-- AtrributeListWrapper


    //-----------------------------/
    //- org.xml.sax.AttributeList -/
    //-----------------------------/

    /**
     * Returns the number of attributes in the list.
     * @return The number of attributes in the list.
    **/
    public int getLength () {
        if (_map == null) return 0;
        return _map.getLength();
    } //-- getLength

    /**
     * Returns the name of the attribute at the given index.
     * @param i The position of the attribute in the list.
     * @return The attribute name as a string, or null if there
     *         is no attribute at that position.
    **/
    public String getName (int i) {
        if (_map != null) {
            Attr attr = (Attr)_map.item(i);
            return attr.getName();
        }
        return null;
    } //-- getName


    /**
     * Returns the type of the attribute at the specified index.
     * @param i The position of the attribute in the list.
     * @return The attribute type as a string ("NMTOKEN" for an
     *         enumeration, and "CDATA" if no declaration was
     *         read), or null if there is no attribute at
     *         that position.
     * <br/><b>Note:</b> Not supported, will simply return null.
    **/
    public String getType (int i) {
        return null;
    } //-- getType

    /**
     * Return the value of the attribute at the specified index
     * @param i The position of the attribute in the list.
     * @return The attribute value as a string, or null if
     *         there is no attribute at that position.
    **/
    public String getValue (int i) {
        
        if (_map != null) {
            Attr attr = (Attr)_map.item(i);
            return attr.getValue();
        }
        return null;

    } //-- getValue

    /**
     * Return the type of the attribute with the given name.
     * @param name The attribute name.
     * @return The attribute type as a string ("NMTOKEN" for an
     *         enumeration, and "CDATA" if no declaration was
     *         read).
     * <br/><b>Note:</b> Not supported, will simply return null.
    **/
    public String getType (String name) {
        return null;
    } //-- getType


    /**
     * Get the value of an attribute (by name).
     *
     * @param name The attribute name.
     * @see org.xml.sax.AttributeList#getValue(java.lang.String)
    **/
    public String getValue (String name) {
        if (_map != null) {
            Attr attr = (Attr) _map.getNamedItem(name);
            if (attr != null) return attr.getValue();
        }
        return null;
    } //-- getValue

} //-- AttributeListWrapper