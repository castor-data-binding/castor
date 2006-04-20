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
 * Copyright 2000 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
package jdo;

import java.util.Arrays;


/**
 * Test object for different collection types.
 */
public class TestSerializableObject implements java.io.Serializable {

    public String aCoolString;

    public int[] ints;

    public TestSerializableObject() {
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("<");
        sb.append(aCoolString);
        sb.append("--");
        if ( ints == null )
            sb.append("empty int[]");
        else
            for ( int i=0; i < ints.length; i++ ) {
                if ( i != 0 ) sb.append(", ");
                sb.append(ints[i]);
            }

        return sb.toString();
    }

    public boolean equals( Object object ) {
        if ( object == null )
            return false;

        if ( this == object )
            return true;

        if ( !(object instanceof TestSerializableObject) )
            return false;
        
        TestSerializableObject obj = (TestSerializableObject) object;

        if ( aCoolString == null && obj.aCoolString != null )
            return false;

        if ( aCoolString != null && obj.aCoolString == null )
            return false;

        if ( !aCoolString.equals( obj.aCoolString ) )
            return false;

        return Arrays.equals( ints, obj.ints );
    }
}