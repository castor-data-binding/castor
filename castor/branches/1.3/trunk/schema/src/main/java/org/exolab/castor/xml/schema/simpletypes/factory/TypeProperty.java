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

import java.io.PrintWriter;

/**
 * Stores a "facet" of an xml built in type. The facet may not be defined in the spec.
 *
 * It is public only because castor is used to unmarshall it from a config file
 * So please consider this class PRIVATE.
 */
public class TypeProperty
{
    /**
     * name of the facet
     */
    private String name= null;

    /**
     * value of the facet
     */
    private String value= null;

    /**
     * Tells if the facet id genuine (defined by the schema spec and not
     * added for convenience like for RealType).
     */
    private boolean pseudo= false;

    public String  getName()   { return name;}
    public String  getValue()  { return value;}
    public boolean getPseudo() { return pseudo;}

    public void setName(String name)      { this.name = name; }
    public void setValue(String value)    { this.value = value; }
    public void setPseudo(boolean pseudo) { this.pseudo= pseudo;}

    public TypeProperty()
    {
    }

    public void Print(PrintWriter output)
    {
        output.println(toString());
    }
    
    /**
     * To generate a {@link String} representing this class instance.
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return name + " : " + value;
    }
}
