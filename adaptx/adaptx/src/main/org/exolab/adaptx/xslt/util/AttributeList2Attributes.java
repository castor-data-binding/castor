/*
 * (C) Copyright Keith Visco 2003  All rights reserved.
 *
 * The contents of this file are released under an Open Source 
 * Definition (OSD) compliant license; you may not use this file 
 * execpt in compliance with the license. Please see license.txt, 
 * distributed with this file. You may also obtain a copy of the
 * license at http://www.kvisco.com/xslp/license.txt
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

package org.exolab.adaptx.xslt.util;


import org.xml.sax.Attributes;
import org.xml.sax.AttributeList;

/**
 * An implementation of the SAX 2.0 Attributes interface
 * which wraps a SAX 1.0 AttributeList
 *
 * @author <a href="mailto:keith@kvisco.com">Keith Visco</a>
 * @version $REVISION$ $DATE$
 */
public class AttributeList2Attributes
    implements Attributes
{

    private AttributeList _internalAtts = null;
    
    /**
     * Creates a new AttributeList2Attributes instance
     * with the given AttributeList
     *
     * @param atts the AttributeList to wrap
     */
    public AttributeList2Attributes(AttributeList atts) {
        if (atts == null) {
            String error = "The argument 'atts' must not be null.";
            throw new IllegalArgumentException(error);
        }
        _internalAtts = atts;
    } //-- AttributeList2Attributes
    
    /**
     * Returns the AttributeList being "wrapped" by this
     * AttributeList2Attributes instance.
     *
     * @return the AttributeList being "wrapped".
     */
    public AttributeList getAttributeList() {
        return _internalAtts;
    } //-- getAttributeList
    
    /**
     * Return the number of attributes in the list.
     *
     * <p>Once you know the number of attributes, you can iterate
     * through the list.</p>
     *
     * @return The number of attributes in the list.
     */
    public int getLength () {
        return _internalAtts.getLength();
    } //-- getLength


    /**
     * Look up an attribute's Namespace URI by index.
     *
     * @param index The attribute index (zero-based).
     * @return The Namespace URI, or the empty string if none
     *         is available, or null if the index is out of
     *         range.
     * @see #getLength
     */
    public String getURI (int index) {
        if ((index >= 0) && (index < _internalAtts.getLength()))
            return "";
        return null;
    } //-- getURI


    /**
     * Look up an attribute's local name by index.
     *
     * @param index The attribute index (zero-based).
     * @return The local name, or the empty string if Namespace
     *         processing is not being performed, or null
     *         if the index is out of range.
     * @see #getLength
     */
    public String getLocalName (int index) {
        if ((index >= 0) && (index < _internalAtts.getLength()))
            return "";
        return null;        
    } //-- getLocalName


    /**
     * Look up an attribute's XML 1.0 qualified name by index.
     *
     * @param index The attribute index (zero-based).
     * @return The XML 1.0 qualified name, or the empty string
     *         if none is available, or null if the index
     *         is out of range.
     * @see #getLength
     */
    public String getQName (int index) {
        if ((index >= 0) && (index < _internalAtts.getLength()))
            return _internalAtts.getName(index);
        return null;
    } //-- getQName


    /**
     * Look up an attribute's type by index.
     *
     * <p>The attribute type is one of the strings "CDATA", "ID",
     * "IDREF", "IDREFS", "NMTOKEN", "NMTOKENS", "ENTITY", "ENTITIES",
     * or "NOTATION" (always in upper case).</p>
     *
     * <p>If the parser has not read a declaration for the attribute,
     * or if the parser does not report attribute types, then it must
     * return the value "CDATA" as stated in the XML 1.0 Recommentation
     * (clause 3.3.3, "Attribute-Value Normalization").</p>
     *
     * <p>For an enumerated attribute that is not a notation, the
     * parser will report the type as "NMTOKEN".</p>
     *
     * @param index The attribute index (zero-based).
     * @return The attribute's type as a string, or null if the
     *         index is out of range.
     * @see #getLength
     */
    public String getType (int index) {
        if ((index >= 0) && (index < _internalAtts.getLength()))
            return _internalAtts.getType(index);
        return null;
    } //-- getType


    /**
     * Look up an attribute's value by index.
     *
     * <p>If the attribute value is a list of tokens (IDREFS,
     * ENTITIES, or NMTOKENS), the tokens will be concatenated
     * into a single string with each token separated by a
     * single space.</p>
     *
     * @param index The attribute index (zero-based).
     * @return The attribute's value as a string, or null if the
     *         index is out of range.
     * @see #getLength
     */
    public String getValue (int index) {
        if ((index >= 0) && (index < _internalAtts.getLength()))
            return _internalAtts.getValue(index);
        return null;
    } //-- getValue

    /**
     * Look up the index of an attribute by Namespace name.
     *
     * @param uri The Namespace URI, or the empty string if
     *        the name has no Namespace URI.
     * @param localName The attribute's local name.
     * @return The index of the attribute, or -1 if it does not
     *         appear in the list.
     */
    public int getIndex (String uri, String localPart) {
        return -1;
    } //-- getIndex


    /**
     * Look up the index of an attribute by XML 1.0 qualified name.
     *
     * @param qName The qualified (prefixed) name.
     * @return The index of the attribute, or -1 if it does not
     *         appear in the list.
     */
    public int getIndex (String qName) {
        if (qName == null) return -1;
        for (int i = 0; i < _internalAtts.getLength(); i++) {
            if (qName.equals(_internalAtts.getName(i)))
                return i;
        }
        return -1;
    } //-- getIndex


    /**
     * Look up an attribute's type by Namespace name.
     *
     * <p>See {@link #getType(int) getType(int)} for a description
     * of the possible types.</p>
     *
     * @param uri The Namespace URI, or the empty String if the
     *        name has no Namespace URI.
     * @param localName The local name of the attribute.
     * @return The attribute type as a string, or null if the
     *         attribute is not in the list or if Namespace
     *         processing is not being performed.
     */
    public String getType (String uri, String localName) {
        return null;
    } //-- getType


    /**
     * Look up an attribute's type by XML 1.0 qualified name.
     *
     * <p>See {@link #getType(int) getType(int)} for a description
     * of the possible types.</p>
     *
     * @param qName The XML 1.0 qualified name.
     * @return The attribute type as a string, or null if the
     *         attribute is not in the list or if qualified names
     *         are not available.
     */
    public String getType (String qName) {
        return _internalAtts.getType(qName);
    } //-- getType


    /**
     * Look up an attribute's value by Namespace name.
     *
     * <p>See {@link #getValue(int) getValue(int)} for a description
     * of the possible values.</p>
     *
     * @param uri The Namespace URI, or the empty String if the
     *        name has no Namespace URI.
     * @param localName The local name of the attribute.
     * @return The attribute value as a string, or null if the
     *         attribute is not in the list.
     */
    public String getValue (String uri, String localName) {
        return null;
    } //-- getValue


    /**
     * Look up an attribute's value by XML 1.0 qualified name.
     *
     * <p>See {@link #getValue(int) getValue(int)} for a description
     * of the possible values.</p>
     *
     * @param qName The XML 1.0 qualified name.
     * @return The attribute value as a string, or null if the
     *         attribute is not in the list or if qualified names
     *         are not available.
     */
    public String getValue (String qName) {
        return _internalAtts.getValue(qName);
    } //-- getValue

} //-- AttributeList2Attributes
