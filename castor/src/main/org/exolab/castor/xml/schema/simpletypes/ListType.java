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
 * Copyright 1999-2002 (C) Intalio Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema.simpletypes;

import org.exolab.castor.xml.schema.Annotation;
import org.exolab.castor.xml.schema.SimpleType;
import org.exolab.castor.xml.schema.Schema;
import org.exolab.castor.xml.schema.SchemaException;
import org.exolab.castor.xml.schema.Structure;

/**
 * Represents a SimpleType that is a "list" of a given
 * SimpleType.
 *
 * @author <a href="mailto:berry@intalio.com">Arnaud Berry</a>
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Id$
**/
public class ListType extends SimpleType
{
    
    /**
     * The local annotation for this ListType
    **/
    private Annotation _annotation = null;
    
    /**
     *  The SimpleType the list is based on.
    **/
    private SimpleType _itemType= null;


    private boolean _hasReference = false;
    
    /**
     * Creates a new ListType.
     *
     * @param schema the Schema for this ListType (Cannot be null)
    **/
    public ListType(Schema schema) 
        throws SchemaException
    {
        super();
        
        if (schema == null) {
            String err = "The Schema argument to the constructor of ListType "+
                "may not be null.";
            throw new IllegalArgumentException(err);
        }
        super.setSchema(schema);
    } //-- ListType


    /** 
     * Returns the simpleType for the items of this ListType.
     *
     * @return the simpleType for the items of this ListType.
    **/
    public SimpleType getItemType() { 
        if (_hasReference) {
            SimpleType simpleType = resolveReference(_itemType);
            if (simpleType == null) {
                String err = "Unable to resolve type: " + _itemType.getName();
                throw new IllegalStateException(err);
            }
            _hasReference = false;
            _itemType = simpleType;
        }
        return _itemType; 
    } //-- getItemType

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
     * Returns the type of this Schema Structure
     * @return the type of this Schema Structure
    **/
    public short getStructureType() {
        return Structure.SIMPLE_TYPE; //-- should be changed to LIST
    } //-- getStructureType
    
    /** 
     * Sets the SimpleType for this ListType (the type of
     * item that instances of this list holds).
     *
     * @param type the SimpleType for this ListType.
    **/
    public void setItemType(SimpleType type) { 
        _itemType = type; 
        _hasReference = false;
    } //-- setItemType
    
    /** 
     * Sets the SimpleType for this ListType (the type of
     * item that instances of this list holds).
     *
     * @param typeName the name of the SimpleType for this ListType.
    **/
    public void setItemType(String typeName) { 
        if (typeName == null) {
            _itemType = null;
            _hasReference = false;
        }
        else {
            _itemType = createReference(typeName); 
            _hasReference = true;
        }
    } //-- setItemType
    /**
     * Sets an annotation which is local to this Union.
     *
     * @param annotation the local annotation to set for this Union.
    **/
    public void setLocalAnnotation(Annotation annotation) {
        _annotation = annotation;
    } //-- setLocalAnnotation
    
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

} //-- ListType


