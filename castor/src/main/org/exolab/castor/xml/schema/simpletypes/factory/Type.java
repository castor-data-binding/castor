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
 * Copyright 1999-2000 (C) Intalio Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema.simpletypes.factory;

import java.util.Vector;
import java.io.PrintWriter;

import org.exolab.castor.xml.schema.SimpleType;

/**
 * Stores information about an xml built in type.
 *
 */
public class Type
{
    /**
     * The name of the built in type
     */
    private String name = null;

    /**
     * The code of the built in type
     * (name of the corresponding field in SimpleTypesFactory)
     */
    private String code = null;

    /**
     * The name of the base type (null for primitive types)
     */
    private String base = null;

    /**
     * The name of the implementing class
     * (in org.exolab.castor.xml.schema.simpletypes)
     */
    private String impl = null;

    /**
     * The name of the derivation method.
     */
    private String derivedBy = null;

    /**
     * The class used to represent this type
     */
    private Class  implClass= null;

    /**
     * The instance representing this type
     */
    private SimpleType simpleType= null;

    /**
     * This type's properties ("facet" like)
     * Vector<TypeProperty>
     */
    private Vector facet= new Vector(15);

    public String     getName      ()   { return name; }
    public String     getCode      ()   { return code; }
    public String     getBase      ()   { return base; }
    public String     getImpl      ()   { return impl; }
    public String     getDerivedBy ()   { return derivedBy; }
    public Vector     getFacet     ()   { return facet;}
    public Class      getImplClass ()   { return implClass; }
    public SimpleType getSimpleType()   { return simpleType;}


    public void       setName      (String name)           { this.name  = name; }
    public void       setCode      (String code)           { this.code  = code; }
    public void       setBase      (String base)           { this.base  = base; }
    public void       setDerivedBy (String derivedBy)      { this.derivedBy  = derivedBy; }
    public void       setSimpleType(SimpleType simpleType) { this.simpleType = simpleType;}

    /**
     * Sets the implementing class name
     * and tries to create the corresponding class in the package
     * org.exolab.castor.xml.schema.simpletypes
     */
    public void setImpl (String impl)
    {
        this.impl= impl;
        try
        {
            String fullName= "org.exolab.castor.xml.schema.simpletypes." + impl;
	        implClass= Class.forName(fullName);
        }
        catch (Exception e)
        {
            implClass= null;
        }
    }

    /**
     * Prints this instance's content
     */
    public void Print(PrintWriter output)
    {
        output.println("name: " + name + " code: " + code + " base: " + base  + " impl: " + impl + " derivedBy: " + derivedBy);
        output.println("Facets count: " + facet.size());

        for( int index= 0; index < facet.size(); index++)
        {
            ((TypeProperty)(facet.elementAt(index))).Print(output);
        }
        output.println();
    }
}
