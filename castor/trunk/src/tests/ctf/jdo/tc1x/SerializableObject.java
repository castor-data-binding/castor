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

package ctf.jdo.tc1x;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Test object to be serialized when storing in database.
 */
public final class SerializableObject implements Serializable {
    /** SerialVersionUID */
    private static final long serialVersionUID = 967532244296334192L;
    
    private String  _string;
    private int[]   _ints;
    
    public void setString(final String string) { _string = string; }
    public String getString() { return _string; }
    
    public void setInts(final int[] ints) { _ints = ints; }
    public int[] getInts() { return _ints; }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("<");
        sb.append(_string);
        sb.append("--");
        if (_ints == null) {
            sb.append("empty int[]");
        } else {
            for (int i = 0; i < _ints.length; i++) {
                if (i != 0) { sb.append(", "); }
                sb.append(_ints[i]);
            }
        }

        return sb.toString();
    }
    
    public int hashCode() {
        int hash = 0;
        if (_string != null) { hash += _string.hashCode(); }
        if (_ints != null) { hash += _ints.hashCode(); }
        return hash;
    }

    public boolean equals(final Object object) {
        if (object == null) { return false; }
        if (this == object) { return true; }
        if (!(object instanceof SerializableObject)) { return false; }
        
        SerializableObject obj = (SerializableObject) object;

        if ((_string == null) && (obj._string != null)) { return false; }
        if ((_string != null) && (obj._string == null)) { return false; }
        if (!_string.equals(obj._string)) { return false; }

        return Arrays.equals(_ints, obj._ints);
    }
}
