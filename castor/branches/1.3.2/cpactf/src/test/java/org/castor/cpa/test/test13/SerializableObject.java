/*
 * Copyright 2009 Udai Gupta, Ralf Joachim
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.castor.cpa.test.test13;

import java.io.Serializable;
import java.util.Arrays;

import org.junit.Ignore;

/**
 * Test object to be serialized when storing in database.
 */
@Ignore
public final class SerializableObject implements Serializable {
    /** SerialVersionUID */
    private static final long serialVersionUID = 967532244296334192L;
    
    private String _string;
    private int[] _ints;
    
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
