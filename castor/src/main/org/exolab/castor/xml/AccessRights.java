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
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml;

/**
 * A class used to indicate access rights
 * @author <a href="kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class AccessRights {
    
    /**
     * The type that indicates both read and write access
    **/
    public static final short BOTH  = 0;
    
    /**
     * The type that indicates only read access
    **/
    public static final short READ = 1;
    
    /**
     * The type that indicates only write access
    **/
    public static final short WRITE = 2;
    
    
    /**
     * A read and write AccessRights
    **/
    public static final AccessRights both 
        = new AccessRights(AccessRights.BOTH);
        
    /**
     * A read-only AccessRights
    **/
    public static final AccessRights read
        = new AccessRights(AccessRights.READ);
    
    /**
     * A write-only AccessRights
    **/
    public static final AccessRights write 
        = new AccessRights(AccessRights.WRITE);
    
    /**
     * The type of this AccessRights
    **/
    private short type = BOTH;
    
    /**
     * Creates a default AccessRights with both read and write privileges
    **/
    private AccessRights() {
        super();
    } //-- AccessRights
    
    /**
     * Creates a new AccessRights with the privilege set to the given type
     * @param type the type of privilege to be granted
    **/
    private AccessRights(short type) {
        this.type = type;
    } //-- AccessRights
    
    /**
     * Returns the type of this Access
    **/
    public short getType() {
        return type;
    } //-- getType
    
    
    /**
     * Returns true if this AccessRights allows reading
     * @return true if this AccessRights allows reading,
     * otherwise false.
    **/
    public boolean isReadable() {
        return ((type == BOTH) || (type == READ));
    } //-- isReadable
    
    /**
     * Returns true if this AccessRights allows writing
     * @return true if this AccessRights allows writing, 
     * otherwise false.
    **/
    public boolean isWritable() {
        return ((type == BOTH) || (type == WRITE));
    } //-- isWritable

    
} //-- AccessRights
