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


/**
 * A class that represents the XML Schema Identity Constraint: KeyRef.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2006-04-14 04:14:43 -0600 (Fri, 14 Apr 2006) $
**/
public class KeyRef extends IdentityConstraint {
    /** SerialVersionUID */
    private static final long serialVersionUID = -7342572522733089648L;

    /**
     * The refer for the KeyRef, (ie. the name of key being refered to).
    **/
    private String _refer = null;
    
    /**
     * Creates a new KeyRef Identity-Constraint.
     *
     * @param name, the name for the IdentityConstraint. Must not be null.
     * @exception SchemaException when name or refer are null.
    **/
    public KeyRef(String name, String refer) 
        throws SchemaException
    {
        super(name);
        setRefer(refer);
    } //-- KeyRef

    /**
     * Returns the refer field (the name of the key being referenced).
     *
     * @return the refer field (the name of the key being referenced).
    **/
    public String getRefer() { 
        return _refer;
    } //-- getRefer
    
    /**
     * Sets the refer field (the name of the key being referenced).
     *
     * @param refer the name of the key to reference. Must not be null.
     * @exception SchemaException when refer is null.
    **/
    public void setRefer(String refer) 
        throws SchemaException
    {
        if (refer == null)
            throw new SchemaException("The 'refer' field of a KeyRef must not be null.");
        _refer = refer;
    } //-- setRefer
    
    /**
     * Returns the type of this Schema Structure.
     *
     * @return the type of this Schema Structure.
    **/
    public short getStructureType() {
        return Structure.KEYREF;
    } //-- getStructureType

} //-- class: KeyRef