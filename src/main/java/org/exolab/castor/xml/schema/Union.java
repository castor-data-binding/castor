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
 * Copyright 2001 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema;

import java.util.Enumeration;
import java.util.Vector;

/**
 * A class that represents the XML Schema Union simple-type.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2006-04-14 04:14:43 -0600 (Fri, 14 Apr 2006) $
**/
public class Union extends SimpleType {
    /** SerialVersionUID */
    private static final long serialVersionUID = -1389185884142578168L;

    /**
     * The local annotation for this Union
    **/
    private Annotation _annotation = null;
    
    /**
     * The id attribute for this Union
    **/
    private String _id = null;
    
    /**
     * The simpleType members of this Union
    **/
    private Vector _simpleTypes = null;
        
    private boolean _hasReferencedTypes = false;
    
    /**
     * Creates a new Union type.
     *
     * @param schema the Schema for this Union (Cannot be null)
    **/
    public Union(Schema schema) 
        throws SchemaException
    {
        super();
        
        if (schema == null) {
            String err = "The Schema argument to the constructor of Union "+
                "may not be null.";
            throw new IllegalArgumentException(err);
        }
        super.setSchema(schema);
        //-- No sense in having a union of only one (1), but
        //-- we choose a low number, like two (2) or three (3)
        //-- since most unions will only have at most a 
        //-- few members.
        _simpleTypes = new Vector(3);
    } //-- Union
    
    /**
     * Adds the given SimpleType reference as a member of this
     * Union. An exception will be thrown during a call to 
     * #getMemberTypes if this reference cannot be resolved.
     *
     * @param typeName the name of the SimpleType to add.
    **/
    public void addMemberType(String typeName) {
        if (typeName == null) return;
        addMemberType(new SimpleTypeReference(getSchema(), typeName));
    } //-- addMemberType
    
    /**
     * Adds the given SimpleType as a member of this Union
     *
     * @param simpleType the SimpleType to add to this Union.
    **/
    public void addMemberType(SimpleType simpleType) {
        if (simpleType == null) return;
        
        if (simpleType instanceof SimpleTypeReference) {
            if (simpleType.getType() != null) {
                simpleType = (SimpleType)simpleType.getType();
            }
            else _hasReferencedTypes = true;
        }
        _simpleTypes.add(simpleType);
    } //-- addMemberType
    
    /**
     * Returns the id for this Union, or null if no id has been set.
     *
     * @return the id for this Union, or null if no id has been set..
    **/
    public String getId() {
        return _id;
    } //-- getId
    
    /**
     * Returns the annotation which appears local to this Union, or
     * null if no local annotation has been set.
     *
     * @return the annotation which is local to this Union. 
    **/
    public Annotation getLocalAnnotation() {
        return _annotation;
    } //-- getLocalAnnotation
    
    /**
     * Returns an Enumeration of all the SimpleTypes that are members of 
     * this Union.
     *
     * @return an Enumeration of all member SimpleTypes.
    **/
    public Enumeration getMemberTypes() {
        //-- clear any referenced types (if necessary)
        if (_hasReferencedTypes) {
            _hasReferencedTypes = false;
            for (int i = 0; i < _simpleTypes.size(); i++) {
                Object obj = _simpleTypes.elementAt(i);
                if (obj instanceof SimpleTypeReference) {
                    SimpleType simpleType = (SimpleType)obj;
                    if (simpleType.getType() != null) {
                        _simpleTypes.setElementAt(simpleType.getType(), i);
                    }
                    else {
                        //-- XXXX
                        //-- we should really throw an exception here
                        //-- but for now...just re-mark as having
                        //-- an unresolved referred type.
                        _hasReferencedTypes = true;
                    }
                }
            }
        }
        return _simpleTypes.elements();
    } //-- getMemberTypes;
    
    /**
     * Returns the type of this Schema Structure
     * @return the type of this Schema Structure
    **/
    public short getStructureType() {
        return Structure.UNION;
    } //-- getStructureType
    
    /**
     * Sets the Schema for this Union. This method overloads the 
     * SimpleType#setSchema method to prevent the Schema from being
     * changed.
     *
     * @param the schema that this Union belongs to.
    **/
    public void setSchema(Schema schema) {
        if (schema != getSchema()) {
            String err = "The Schema of an Union cannot be changed.";
            throw new IllegalStateException(err);
        }
    } //-- void setSchema
    
    /**
     * Sets the id for this Union.
     * 
     * @param id the unique id for this Union. Must be globally unique 
     * within the scope of the Schema.
    **/
    public void setId(String id) {
        _id = id;
    } //-- setId
    
    /**
     * Sets an annotation which is local to this Union.
     *
     * @param annotation the local annotation to set for this Union.
    **/
    public void setLocalAnnotation(Annotation annotation) {
        _annotation = annotation;
    } //-- setLocalAnnotation
    
} //-- class: Union