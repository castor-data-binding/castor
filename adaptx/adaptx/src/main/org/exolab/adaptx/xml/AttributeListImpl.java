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
 * $Id$
 */
package org.exolab.adaptx.xml;


import org.xml.sax.AttributeList;
import org.exolab.adaptx.util.List;

/**
 * my own implementation of the SAX AttributeList
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class AttributeListImpl implements AttributeList {


    List names  = null;
    List values = null;
    
    public AttributeListImpl() {
        names  = new List(7);
        values = new List(7);
    } //-- AtrributeListImpl

    public AttributeListImpl(int size) {
        names  = new List(size);
        values = new List(size);
    } //-- AtrributeListImpl

    public void addAttribute(String name, String value) {
        if (name == null) return;
        int idx = names.indexOf(name);
        if (idx >= 0) {
            if (value == null) {
                names.remove(idx);
                values.remove(idx);
            }
            else {
                values.set(idx, value);
            }
        }
        else {
            names.add(name);
            values.add(value);
        }
    } //-- addAttribute
    
    /**
     * Removes all attribute mappings from this AttributeList
    **/
    public void clear() {
        names.clear();
        values.clear();
    } //-- clear
    
    /**
     * Removes the name-value pair at the given index
     * @param index the index to remove the name value pair from
    **/
    public void remove(int index) {
        names.remove(index);
        values.remove(index);
    } //-- remove

    /**
     * Removes the name and it's associated value from this AttributeList
     * @param the name of the attribute mapping to remove
    **/
    public void remove(String name) {
        int idx = names.indexOf(name);
        if (idx >= 0) remove(idx);
    } //-- remove
    
    //-----------------------------/
    //- org.xml.sax.AttributeList -/
    //-----------------------------/
  
    /**
     * Returns the number of attributes in the list.
     * @return The number of attributes in the list.
    **/
    public int getLength () {
        return names.size();
    } //-- getLength


    /**
     * Returns the name of the attribute at the given index.
     * @param i The position of the attribute in the list.
     * @return The attribute name as a string, or null if there
     *         is no attribute at that position.
    **/
    public String getName (int i) {
        return (String) names.get(i);
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
        return (String) values.get(i);
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
        int idx = names.indexOf(name);
        if (idx >= 0) {
            return (String) values.get(idx);
        }
        return null;
    } //-- getValue

} //-- AttributeListImpl
