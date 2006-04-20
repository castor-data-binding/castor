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
 * Copyright 1999-2003 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema;

import org.exolab.castor.types.AnyNode;
import org.exolab.castor.xml.ValidationException;

import java.util.Enumeration;
import java.util.Vector;

/**
 * A class which represents the superclass of
 * either AppInfo or Documentation element.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$ 
 */
public abstract class AnnotationItem extends Structure {
    
    
    /**
     * List of any elements
    **/
    private Vector _objects = null;
    
    /**
     * The source attribute
    **/
    private String _source = null;
    
    /**
     * Creates a new AnnotationItem
     */
    AnnotationItem() {
        _objects = new Vector(3);
    } //-- AnnotationItem
    
    /**
     * Adds the given Object to this Annotation item.
     *
     * @param object the Object to add
     */
    public void add(Object object) {
        if (object != null)
            _objects.addElement(object);
    } //-- add
    
    /**
     * Returns the String content of this Annotation item.
     *
     * @return the String content of this Annotation item.
     */
    public String getContent() {
        if (_objects.size() == 0) return null;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < _objects.size(); i++) {
            Object obj = _objects.elementAt(i);
            if (obj instanceof AnyNode) {
                //-- the getStringValue of AnyNode is a bit messed up
                //-- so we'll do our own here
                getStringValue((AnyNode)obj, sb);
            }
            else {
                sb.append(obj.toString());
            }
        }
        return sb.toString();
    } //-- getContent
    
    /**
     * Returns an Enumeration of all objects contained by this Annotation item.
     *
     * @return an Enumeration of all objects contained by this Annotation item.
     */
    public Enumeration getObjects() {
        return _objects.elements();
    } //-- getObjects
    
    /**
     * Returns the source property of this Annotaion item.
     *
     * @return the source property of this Annotation item.
     */
    public String getSource() {
        return _source;
    } //-- getSource
    
    /**
     * Removes the given Object from this Annotation item.
     *
     * @param object the Object to remove
     */
    public void remove(Object object) {
        if (object != null) _objects.removeElement(object);
    } //-- remove

    /**
     * Sets the source property for this Annotaion item.
     *
     * @param source the value of the source property
     */
    public void setSource(String source) {
        _source = source;
    } //-- setSource
    
    //-------------------------------/
    //- Implementation of Structure -/
    //-------------------------------/
    
    /**
     * Returns the type of this Schema Structure
     *
     * @return the type of this Schema Structure
     */
    public abstract short getStructureType();
    
    /**
     * Checks the validity of this Schema defintion.
     * @exception ValidationException when this Schema definition
     * is invalid.
    **/
    public void validate()
        throws ValidationException 
    {
        //-- do nothing
    } //-- validate
    
    /**
     * Returns the concatenation of all the TEXT nodes in the given
     * AnyNode in document order
     *
     * @param node the AnyNode to return the String value of
     * @param buffer the StringBuffer to append to.
     */
    static final void getStringValue(AnyNode node, StringBuffer buffer) {
        switch(node.getNodeType()) {
            case AnyNode.ELEMENT:
                AnyNode child = node.getFirstChild();
                while (child != null) {
                    getStringValue(child, buffer);
                    child = child.getNextSibling();
                }
                break;
            case AnyNode.TEXT:
                buffer.append(node.getStringValue());
                break;
            default:
                break;
        }
    } //-- getStringValue
    
} //-- AnnotationItem