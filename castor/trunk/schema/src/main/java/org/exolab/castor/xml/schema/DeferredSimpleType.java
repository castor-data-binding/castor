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

package org.exolab.castor.xml.schema;


/**
 * Package private class to handle the case where a simple type
 * can't be instanciated because its parent has not yet been read.
 *
 * @author <a href="mailto:berry@intalio.com">Arnaud Berry</a>
 * @version $Revision:
**/
class DeferredSimpleType extends SimpleType {
    /** SerialVersionUID */
    private static final long serialVersionUID = 1335439479275580848L;

    /**
     * An instance of ListType or of a class derived from AtomicType
     * created the first time getType is called from the information contained
     * in this simple type instance.
    **/
    private SimpleType instance= null;

    /**
     * The parent's name.
    **/
    private String baseTypeName = null;

    /**
     * The number of times createInstance was called (to guess when there is a cyclic
     * reference (type A extends B; type B extends A) in the schema types)
     * TODO: real cycle detection.
     */
    private int createInstanceCallsCount= 0;

    /**
     * Creates a new default DeferredSimpleType.
    **/
    DeferredSimpleType() {
        super();
    } //-- DeferredSimpleType
    
    /**
     * Creates a new DeferredSimpleType
     *
     * @param name the name of the SimpleType.
    **/
    DeferredSimpleType(String name) {
        super();
        setName(name);
    } //-- DeferredSimpleType
    
    /**
     * Creates a new DeferredSimpleType
     *
     * @param name the name of the SimpleType.
     * @param schema the parent Schema of the SimpleType.
    **/
    DeferredSimpleType(String name, Schema schema) {
        setName(name);
        setSchema(schema); 
    } //-- DeferredSimpleType
    
    /**
     * Sets the name of the base type that couldn't be resolved
     * when this type was created.
     */
    public void setBaseTypeName(String baseTypeName) {
        this.baseTypeName= baseTypeName;
    }

    /**
     * Package private getter of the simpleType instance.
     */
    XMLType getType() {
        if (instance == null) createInstance();
        return instance;
    }


    /**
     * Resolves the parents of this simple type in order to create the
     * instance of the appropriate class deriving from simple type and fill it.
     */
    protected synchronized void createInstance()
    {
        createInstanceCallsCount++;
        if (createInstanceCallsCount >= 666) {
            String err = "cyclic type definition involving the type: " + getName();
            throw new IllegalStateException(err);
        }

        //create the type, false means we don't want a DeferredSimpleType to be returned.
        instance= Schema.getTypeFactory().createUserSimpleType( getSchema(),
                                                                getName(),
                                                                baseTypeName,
                                                                getDerivationMethod(),
                                                                false);

        if (instance != null) {
           copyFacets(instance);
           instance.setParent(getParent());
        }
    }

    /**
     * Returns Structure.UNKNOWN
     * (This class should not be seen outside AttributeDecl and ElementDecl anyway)
    **/
    public short getStructureType() {
        return Structure.UNKNOWN;
    } //-- getStructureType
}


